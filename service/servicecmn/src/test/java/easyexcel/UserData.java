package easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @create 2022-10-05 10:22
 */
@Data
public class UserData {
    @ExcelProperty("用户编号")
    private int uid;

    @ExcelProperty("用户名称")
    private String username;

}
