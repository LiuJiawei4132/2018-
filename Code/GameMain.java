package D2048;

import javax.swing.*;
import java.awt.*;

public class GameMain {
    public static void main(String[] args) {
        Window window = new Window();
        window.initView();
        window.setTitle("D2048");
        window.getContentPane().setPreferredSize(new Dimension(400, 500)); //对JFrame添加组件的一种方式
        //JFrame直接调用setBackground设置背景色不生效
        window.getContentPane().setBackground(new Color(0xfaf8ef)); // 设置背景颜色
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 退出程序
        window.setResizable(false); //去掉最大化按钮
        window.pack();    //获得最佳大小
        window.setVisible(true);    // 设置可见
    }
}
