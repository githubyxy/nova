package test.Image;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class TableImageGenerator {

    public static BufferedImage generateTableImage(String[] headers, List<String[]> data) {
        // 设置缩放因子来提高分辨率
        float scaleFactor = 2.0f; // 2倍分辨率，可以根据需要调整

        // Create table model
        DefaultTableModel model = new DefaultTableModel(headers, 0);
        for (String[] row : data) {
            model.addRow(row);
        }

        // Create JTable
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight((int)(40 * scaleFactor)); // 增加行高
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);
        table.setBorder(new LineBorder(Color.BLACK, (int)(1 * scaleFactor))); // 加粗边框

        // 为表头创建专门的渲染器
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(new LineBorder(Color.BLACK, (int)(1 * scaleFactor)));
                setForeground(Color.BLACK);
                setBackground(new Color(240, 240, 240)); // 添加浅灰色背景
                setFont(getFont().deriveFont(Font.BOLD)); // 表头字体加粗
                setToolTipText(null);
                return this;
            }
        };
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        // 为数据行创建渲染器
        DefaultTableCellRenderer dataRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                String str = String.valueOf(value);
                boolean f = str.startsWith("-");
                boolean z = str.startsWith("+");
                super.getTableCellRendererComponent(table, (f || z) ? str.substring(1) : str, isSelected, hasFocus, row, column);
                setHorizontalAlignment((row != 0 && column == 0) ? SwingConstants.LEFT : SwingConstants.CENTER);
                setBorder(new LineBorder(Color.BLACK, (int)(1 * scaleFactor)));
                setForeground((!f && !z) ? Color.BLACK : (z ? Color.BLUE : Color.RED));
                setToolTipText(null);
                return this;
            }
        };

        // 应用数据行渲染器到所有列
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(dataRenderer);
        }

        // 设置更大的字体
        Font font = new Font("WenQuanYi Micro Hei", Font.PLAIN, (int)(14 * scaleFactor));
        table.setFont(font);
        table.getTableHeader().setFont(font.deriveFont(Font.BOLD));
        FontRenderContext frc = new FontRenderContext(null, true, true);

        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int maxWidth = getTextWidth(headers[column], font, frc);

            for (int row = 0; row < table.getRowCount(); row++) {
                String cellValue = (String) table.getValueAt(row, column);
                int cellWidth = getTextWidth(cellValue, font, frc);
                maxWidth = Math.max(maxWidth, cellWidth);
            }

            int columnWidth = (int)((maxWidth + 60) * scaleFactor); // 增加列宽和内边距
            tableColumn.setPreferredWidth(columnWidth);
            tableColumn.setMinWidth(columnWidth);
            tableColumn.setMaxWidth(columnWidth);
        }

        // Calculate total dimensions
        Dimension d = table.getPreferredSize();
        Dimension headerD = table.getTableHeader().getPreferredSize();
        int totalHeight = d.height + headerD.height;

        // Create image with some padding
        BufferedImage image = new BufferedImage(
                (int)(d.width * scaleFactor) + 8,
                (int)(totalHeight * scaleFactor) + 8,
                BufferedImage.TYPE_INT_ARGB // 使用ARGB以支持透明度
        );

        // Paint table to image
        Graphics2D g2d = image.createGraphics();
        // 设置渲染提示以获得更好的输出质量
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        // 应用缩放
        g2d.scale(scaleFactor, scaleFactor);

        // Fill background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, d.width + 8, totalHeight + 8);

        // Paint header and table with slight offset to account for padding
        table.getTableHeader().setBounds(4, 4, d.width, headerD.height);
        table.getTableHeader().paint(g2d);

        table.setBounds(4, headerD.height + 4, d.width, d.height);
        table.setOpaque(true);
        table.paint(g2d);
        g2d.dispose();

        return image;
    }

    private static int getTextWidth(String text, Font font, FontRenderContext frc) {
        if (text == null) return 0;
        TextLayout layout = new TextLayout(text, font, frc);
        return (int) layout.getBounds().getWidth();
    }
}