package brackground.dao;

import frontEnd.zxq.entity.TmStoreAdmin;

import java.sql.SQLException;

public interface StorAdminDao {
    public boolean login(TmStoreAdmin tmStoreAdmin) throws SQLException;
    public void reg(TmStoreAdmin tmStoreAdmin) throws SQLException;
    public boolean checkStoreAdminName(String adminname) throws SQLException;
}
