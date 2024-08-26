package com.dkd.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dkd.common.constant.Constants;
import com.dkd.common.constant.DkdContants;
import com.dkd.common.utils.DateUtils;
import com.dkd.common.utils.DesensitizedUtil;
import com.dkd.common.utils.uuid.UUIDUtils;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.VmType;
import com.dkd.manage.service.IChannelService;
import com.dkd.manage.service.INodeService;
import com.dkd.manage.service.IVmTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.VendingMachineMapper;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备管理Service业务层处理
 *
 * @author wang
 * @date 2024-08-25
 */
@Service
@RequiredArgsConstructor
public class VendingMachineServiceImpl implements IVendingMachineService {

    private final VendingMachineMapper vendingMachineMapper;

    private final IChannelService channelService;

    private final INodeService nodeService;

    private final IVmTypeService vmTypeService;

    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    @Override
    public VendingMachine selectVendingMachineById(Long id) {
        return vendingMachineMapper.selectVendingMachineById(id);
    }

    /**
     * 查询设备管理列表
     *
     * @param vendingMachine 设备管理
     * @return 设备管理
     */
    @Override
    public List<VendingMachine> selectVendingMachineList(VendingMachine vendingMachine) {
        return vendingMachineMapper.selectVendingMachineList(vendingMachine);
    }

    /**
     * 新增设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertVendingMachine(VendingMachine vendingMachine) {

        //1.新增设备
        //1-1.设备编号
        String innerCode = UUIDUtils.getUUID();
        vendingMachine.setInnerCode(innerCode);

        //1-2.点位信息
        Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
        BeanUtils.copyProperties(node,vendingMachine,"id");
        vendingMachine.setAddr(node.getAddress());

        //1-3.设备型号
        VmType vmType = vmTypeService.selectVmTypeById(vendingMachine.getVmTypeId());
        vendingMachine.setChannelMaxCapacity(vmType.getChannelMaxCapacity());

        //1-4.设备状态
        vendingMachine.setVmStatus(DkdContants.VM_STATUS_NODEPLOY);
        vendingMachine.setCreateTime(DateUtils.getNowDate());
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        int result = vendingMachineMapper.insertVendingMachine(vendingMachine);

        //2.新增货道
        List<Channel> channelList = new ArrayList<>();
        for (int i = 1; i <= vmType.getVmRow(); i++) {
            for (int j = 1; j <= vmType.getVmCol(); j++) {
                Channel channel = new Channel();
                channel.setChannelCode(i + "-" + j);
                channel.setVmId(vendingMachine.getId());
                channel.setInnerCode(vendingMachine.getInnerCode());
                channel.setMaxCapacity(vmType.getChannelMaxCapacity());
                channel.setCreateTime(DateUtils.getNowDate());
                channel.setUpdateTime(DateUtils.getNowDate());
                channelList.add(channel);
            }
        }
        channelService.batchInsertChannel(channelList);

        return result;
    }

    /**
     * 修改设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    public int updateVendingMachine(VendingMachine vendingMachine) {
        //查询点位
        if (vendingMachine.getNodeId() != null){
            Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
            BeanUtils.copyProperties(node,vendingMachine,"id");
            vendingMachine.setAddr(node.getAddress());
        }
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        return vendingMachineMapper.updateVendingMachine(vendingMachine);
    }

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理主键
     * @return 结果
     */
    @Override
    public int deleteVendingMachineByIds(Long[] ids) {
        return vendingMachineMapper.deleteVendingMachineByIds(ids);
    }

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    @Override
    public int deleteVendingMachineById(Long id) {
        return vendingMachineMapper.deleteVendingMachineById(id);
    }
}
