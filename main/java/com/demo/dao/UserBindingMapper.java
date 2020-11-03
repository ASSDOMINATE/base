package com.demo.dao;

import com.demo.bean.UserBinding;
import com.demo.bean.UserBindingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserBindingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int countByExample(UserBindingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int deleteByExample(UserBindingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int insert(UserBinding record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int insertSelective(UserBinding record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    List<UserBinding> selectByExample(UserBindingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    UserBinding selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") UserBinding record, @Param("example") UserBindingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") UserBinding record, @Param("example") UserBindingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UserBinding record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_binding
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UserBinding record);
}