package com.mmall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class UserAddress implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * ??id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * ????
     */
      private Integer userId;

      /**
     * ??
     */
      private String address;

      /**
     * ??
     */
      private String remark;

      /**
     * ????????1:? 0??
     */
      private Integer isdefault;

      /**
     * ????
     */
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;


}
