package brackground.dao.Imp;

import frontEnd.zxq.entity.TmStoreAdmin;
import org.apache.commons.dbutils.QueryRunner;
import brackground.dao.StorAdminDao;
import utils.C3p0Utils;

import java.sql.SQLException;

public class StoreAdminImp implements StorAdminDao {
    private QueryRunner queryRunner = new QueryRunner(C3p0Utils.getDataSource());
    @Override
    public boolean login(TmStoreAdmin tmStoreAdmin) throws SQLException {
        String sql = "select sdid from tmstoreadmin where name=? and password=?";
        int id = queryRunner.update(sql,tmStoreAdmin.getName(),tmStoreAdmin.getPassword());
        return id>0;
    }

    @Override
    public void reg(TmStoreAdmin tmStoreAdmin) throws SQLException {
        String sql = "insert into tmstoreadmin(sdid,name,password) values(tm_sdid,?,?)";
        queryRunner.update(sql,tmStoreAdmin.getName(),tmStoreAdmin.getPassword());
    }

    @Override
    public boolean checkStoreAdminName(String adminname) {
        return false;
    }
}
