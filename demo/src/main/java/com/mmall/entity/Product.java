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
    public class Product implements Serializable {

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
     * ??
     */
      private String description;

      /**
     * ??
     */
      private Float price;

      /**
     * ??
     */
      private Integer stock;

      /**
     * ??1
     */
      private Integer categoryleveloneId;

      /**
     * ??2
     */
      private Integer categoryleveltwoId;

      /**
     * ??3
     */
      private Integer categorylevelthreeId;

      /**
     * ????
     */
      private String fileName;


}
