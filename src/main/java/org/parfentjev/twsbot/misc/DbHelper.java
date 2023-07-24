package org.parfentjev.twsbot.misc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.parfentjev.twsbot.core.exceptions.DatabaseHelperException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DbHelper<DC, ID> {
    private static final Logger logger = LogManager.getLogger("DbHelper");
    private static JdbcConnectionSource connectionSource;

    private final Dao<DC, ID> dao;

    public DbHelper(Class<DC> dataClass) {
        try {
            if (connectionSource == null) {
                connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + Properties.DATABASE_PATH + "bot.db");
            }

            dao = DaoManager.createDao(connectionSource, dataClass);
            createTable();
        } catch (SQLException | DatabaseHelperException e) {
            logger.error(e);

            throw new RuntimeException(e);
        }
    }

    private void createTable() throws DatabaseHelperException {
        try {
            if (dao.isTableExists()) {
                return;
            }

            TableUtils.createTable(dao);
        } catch (SQLException e) {
            logger.error(e);

            throw new DatabaseHelperException(e);
        }
    }

    public Optional<DC> queryById(ID id) {
        try {
            DC item = dao.queryForId(id);

            return item == null ? Optional.empty() : Optional.of(item);
        } catch (SQLException e) {
            logger.error(e);

            return Optional.empty();
        }
    }

    public void save(DC data) throws DatabaseHelperException {
        try {
            dao.create(data);
        } catch (SQLException e) {
            logger.error(e);

            throw new DatabaseHelperException(e);
        }
    }

    public void delete(DC data) throws DatabaseHelperException {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            logger.error(e);

            throw new DatabaseHelperException(e);
        }
    }

    public List<DC> getAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            logger.error(e);

            return Collections.emptyList();
        }
    }
}
