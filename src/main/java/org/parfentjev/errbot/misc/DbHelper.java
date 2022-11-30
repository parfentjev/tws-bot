package org.parfentjev.errbot.misc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DbHelper<DC, ID> {
    private final Class<DC> dataClass;

    public DbHelper(Class<DC> dataClass) {
        this.dataClass = dataClass;

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
                throw new RuntimeException(e);
            }
        });
    }

    public DC queryById(ID id) {
        return queryData(dao -> {
            try {
                return dao.queryForId(id);
            } catch (SQLException e) {
                e.printStackTrace();

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
                e.printStackTrace();

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
                e.printStackTrace();

                return false;
            }
        });
    }

    public List<DC> getAll() {
        return queryData(dao -> {
            try {
                return dao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();

                return Collections.emptyList();
            }
        });
    }

    private <T> T queryData(Function<Dao<DC, ID>, T> daoFunction) {
        try (JdbcConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + Properties.DATABASE_PATH)) {
            return daoFunction.apply(DaoManager.createDao(connectionSource, dataClass));
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
