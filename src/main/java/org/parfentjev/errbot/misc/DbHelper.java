package org.parfentjev.errbot.misc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DbHelper<DC, ID> {
    private static final Logger logger = LogManager.getLogger("DbHelper");
    private static JdbcConnectionSource connectionSource;

    private final Dao<DC, ID> dao;

    public DbHelper(Class<DC> dataClass) {
        try {
            if (connectionSource == null) {
                connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + Properties.DATABASE_PATH);
            }

            dao = DaoManager.createDao(connectionSource, dataClass);
        } catch (SQLException e) {
            logger.error(e);

            throw new RuntimeException(e);
        }

        createTable();
    }

    private void createTable() {
        queryData(dao -> {
            try {
                if (!dao.isTableExists()) {
                    TableUtils.createTable(dao);

                    return true;
                }

                return false;
            } catch (SQLException e) {
                logger.error(e);

                throw new RuntimeException(e);
            }
        });
    }

    public DC queryById(ID id) {
        return queryData(dao -> {
            try {
                return dao.queryForId(id);
            } catch (SQLException e) {
                logger.error(e);

                return null;
            }
        });
    }

    public Boolean save(DC data) {
        return queryData(dao -> {
            try {
                dao.create(data);

                return true;
            } catch (SQLException e) {
                logger.error(e);

                return false;
            }
        });
    }

    public Boolean delete(DC data) {
        return queryData(dao -> {
            try {
                dao.delete(data);

                return true;
            } catch (SQLException e) {
                logger.error(e);

                return false;
            }
        });
    }

    public List<DC> getAll() {
        return queryData(dao -> {
            try {
                return dao.queryForAll();
            } catch (SQLException e) {
                logger.error(e);

                return Collections.emptyList();
            }
        });
    }

    private <T> T queryData(Function<Dao<DC, ID>, T> daoFunction) {
        return daoFunction.apply(dao);
    }
}
