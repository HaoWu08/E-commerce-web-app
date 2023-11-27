package com.mmall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    public class ProductCategory implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * ??
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * ??
     */
      private String name;

      /**
     * ????id
     */
      private Integer parentId;

      /**
     * ??(1:?? 2??? 3???)
     */
      private Integer type;


}
