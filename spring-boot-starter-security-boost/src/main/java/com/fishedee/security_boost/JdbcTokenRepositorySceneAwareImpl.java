package com.fishedee.security_boost;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class JdbcTokenRepositorySceneAwareImpl implements PersistentTokenRepository {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private SecuritySceneResolver securitySceneResolver;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String tokensBySeriesSql = "select username,series,token,last_used from persistent_logins where series = ? and scene_id = ?";
    private String insertTokenSql = "insert into persistent_logins (username, series, token, last_used,scene_id) values(?,?,?,?,?)";
    private String updateTokenSql = "update persistent_logins set token = ?, last_used = ? where series = ?";
    private String removeUserTokensSql = "delete from persistent_logins where username = ? and scene_id = ?";

    public JdbcTokenRepositorySceneAwareImpl() {
    }

    private JdbcTemplate getJdbcTemplate(){
        return this.jdbcTemplate;
    }

    public void createNewToken(PersistentRememberMeToken token) {
        //先删除旧的User的Token
        //避免存在两个UserName + scene_id的组合token，这样可以突破同时在线数为1的限制
        this.removeUserTokens(token.getUsername());

        //再重新添加新的User的Token
        String scenedId = securitySceneResolver.getSceneId();
        this.getJdbcTemplate().update(this.insertTokenSql, new Object[]{token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate(),scenedId});
    }

    public void updateToken(String series, String tokenValue, Date lastUsed) {
        this.getJdbcTemplate().update(this.updateTokenSql, new Object[]{tokenValue, lastUsed, series});
    }

    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        try {
            String scenedId = securitySceneResolver.getSceneId();
            return (PersistentRememberMeToken)this.getJdbcTemplate().queryForObject(this.tokensBySeriesSql, this::createRememberMeToken, new Object[]{seriesId,scenedId});
        } catch (EmptyResultDataAccessException var3) {
            this.logger.debug(LogMessage.format("Querying token for series '%s' returned no results.", seriesId), var3);
        } catch (IncorrectResultSizeDataAccessException var4) {
            this.logger.error(LogMessage.format("Querying token for series '%s' returned more than one value. Series should be unique", seriesId));
        } catch (DataAccessException var5) {
            this.logger.error("Failed to load token for series " + seriesId, var5);
        }

        return null;
    }

    private PersistentRememberMeToken createRememberMeToken(ResultSet rs, int rowNum) throws SQLException {
        return new PersistentRememberMeToken(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getTimestamp(4));
    }

    public void removeUserTokens(String username) {
        String sceneId = securitySceneResolver.getSceneId();
        this.getJdbcTemplate().update(this.removeUserTokensSql, new Object[]{username,sceneId});
    }

}
