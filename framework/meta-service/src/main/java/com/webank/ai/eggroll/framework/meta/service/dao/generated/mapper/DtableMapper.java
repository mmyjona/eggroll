/*
 * Copyright 2019 The Eggroll Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.ai.eggroll.framework.meta.service.dao.generated.mapper;

import com.webank.ai.eggroll.framework.meta.service.dao.generated.model.DtableExample;
import com.webank.ai.eggroll.framework.meta.service.dao.generated.model.model.Dtable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface DtableMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    long countByExample(DtableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    int deleteByExample(DtableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long tableId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    int insert(Dtable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    int insertSelective(Dtable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    List<Dtable> selectByExampleWithRowbounds(DtableExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    List<Dtable> selectByExample(DtableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    Dtable selectByPrimaryKey(Long tableId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Dtable record, @Param("example") DtableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Dtable record, @Param("example") DtableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Dtable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dtable
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Dtable record);
}