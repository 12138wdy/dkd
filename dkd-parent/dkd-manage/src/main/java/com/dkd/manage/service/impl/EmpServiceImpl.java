package com.dkd.manage.service.impl;

import java.util.List;

import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.Region;
import com.dkd.manage.domain.Role;
import com.dkd.manage.mapper.RegionMapper;
import com.dkd.manage.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.EmpMapper;
import com.dkd.manage.domain.Emp;
import com.dkd.manage.service.IEmpService;

/**
 * 人员列表Service业务层处理
 *
 * @author wang
 * @date 2024-08-23
 */
@Service
@RequiredArgsConstructor
public class EmpServiceImpl implements IEmpService {

    private final EmpMapper empMapper;


    private final RoleMapper roleMapper;

    private final RegionMapper regionMapper;

    /**
     * 查询人员列表
     *
     * @param id 人员列表主键
     * @return 人员列表
     */
    @Override
    public Emp selectEmpById(Long id) {
        return empMapper.selectEmpById(id);
    }

    /**
     * 查询人员列表列表
     *
     * @param emp 人员列表
     * @return 人员列表
     */
    @Override
    public List<Emp> selectEmpList(Emp emp) {
        return empMapper.selectEmpList(emp);
    }

    /**
     * 新增人员列表
     *
     * @param emp 人员列表
     * @return 结果
     */
    @Override
    public int insertEmp(Emp emp) {

        Role role = roleMapper.selectRoleByRoleId(emp.getRoleId());
        if (role != null) {
            emp.setRoleName(role.getRoleName());
            emp.setRoleCode(role.getRoleCode());
        }

        Region region = regionMapper.selectRegionById(emp.getRegionId());
        if (region != null){
            emp.setRegionName(region.getRegionName());
        }

        emp.setCreateTime(DateUtils.getNowDate());
        return empMapper.insertEmp(emp);
    }

    /**
     * 修改人员列表
     *
     * @param emp 人员列表
     * @return 结果
     */
    @Override
    public int updateEmp(Emp emp) {

        Role role = roleMapper.selectRoleByRoleId(emp.getRoleId());
        if (role != null) {
            emp.setRoleName(role.getRoleName());
            emp.setRoleCode(role.getRoleCode());
        }

        emp.setRegionName(regionMapper.selectRegionById(emp.getRegionId()).getRegionName());

        emp.setUpdateTime(DateUtils.getNowDate());
        return empMapper.updateEmp(emp);
    }

    /**
     * 批量删除人员列表
     *
     * @param ids 需要删除的人员列表主键
     * @return 结果
     */
    @Override
    public int deleteEmpByIds(Long[] ids) {
        return empMapper.deleteEmpByIds(ids);
    }

    /**
     * 删除人员列表信息
     *
     * @param id 人员列表主键
     * @return 结果
     */
    @Override
    public int deleteEmpById(Long id) {
        return empMapper.deleteEmpById(id);
    }
}
