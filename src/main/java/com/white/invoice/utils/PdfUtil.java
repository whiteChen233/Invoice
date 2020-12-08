package com.white.invoice.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.white.invoice.model.Invoice;
import com.white.invoice.model.Item;

import lombok.extern.slf4j.Slf4j;

/**
 * PdfUtil: The type Pdf util.
 *
 * @author White
 * @version 1.0
 * @since 2020 /12/04 19:15
 */
@Slf4j
public final class PdfUtil {

    private static final String USER_HOME = System.getProperty("user.home");
    private static final PdfUtil INSTANCE = new PdfUtil();
    private static Font headFont;
    private static Font textFont;
    private static final BaseColor COLOR = new BaseColor(156, 82, 35);

    static {
        try {
            /*
             * 新建一个字体,iText的方法 STSongStd-Light 是字体，在iTextAsian.jar 中以property为后缀
             * UniGB-UCS2-H 是编码，在iTextAsian.jar 中以cmap为后缀 H 代表文字版式是 横版， 相应的 V 代表竖版
             */
            BaseFont bf = BaseFont.createFont("/font/simkai.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            headFont = new Font(bf, 28, Font.NORMAL);
            textFont = new Font(bf, 12, Font.NORMAL);

            headFont.setColor(COLOR);
            textFont.setColor(COLOR);
        } catch (Exception e) {
            log.error("init PdfUtil failed!", e);
        }
    }

    private PdfUtil() {
    }

    /**
     * Get pdf util.
     *
     * @return the pdf util
     */
    public static PdfUtil get() {
        return INSTANCE;
    }

    /**
     * 表格中单元格
     *
     * @param value   the value
     * @param alignV  the align v
     * @param alignH  the align h
     * @param colspan the colspan
     * @param rowspan the rowspan
     * @return pdf p cell
     */
    private PdfPCell createCell(String value, int alignV, int alignH, int colspan, int rowspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(alignV);
        cell.setHorizontalAlignment(alignH);
        cell.setPadding(5f);
        cell.setPaddingBottom(9f);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setPhrase(new Phrase(value, textFont));
        cell.setBorderColor(COLOR);
        return cell;
    }

    /**
     * 建段落
     *
     * @param value the value
     * @param font  the font
     * @param align the align
     * @return paragraph
     */
    private Paragraph createParagraph(String value, Font font, int align) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(value, font));
        paragraph.setAlignment(align);
        return paragraph;
    }

    private PdfPTable createTable(PdfPTable table) {
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(10);
        table.setWidthPercentage(100);
        return table;
    }

    /**
     * Generate pdf.
     *
     * @param file    the file
     * @param invoice the invoice
     * @return the boolean
     */
    public boolean generatePdf(File file, Invoice invoice) {
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // 页头信息
            document.add(createParagraph("XX增值税专用发票", headFont, Element.ALIGN_CENTER));
            document.add(createParagraph("征收大厅编码:", textFont, Element.ALIGN_LEFT));
            document.add(createParagraph("执收单位编码:", textFont, Element.ALIGN_LEFT));
            document.add(createParagraph("执收单位名称:", textFont, Element.ALIGN_LEFT));
            document.add(createParagraph(DateTimeFormatter.ofPattern(" yyyy 年 MM 月 dd 日").format(LocalDate.now()), textFont, Element.ALIGN_RIGHT));

            // 表格信息
            PdfPTable table = createTable(new PdfPTable(30));

            table.addCell(createCell("付款人", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 1, 3));

            table.addCell(createCell("全         称", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 4, 1));
            table.addCell(createCell(invoice.getPayer().getName(), Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, 10, 1));

            table.addCell(createCell("收款人", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 1, 3));

            table.addCell(createCell("全         称", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 4, 1));
            table.addCell(createCell(invoice.getPayee().getName(), Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, 10, 1));

            table.addCell(createCell("账         号", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 4, 1));
            table.addCell(createCell(invoice.getPayer().getAccount(), Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, 10, 1));

            table.addCell(createCell("账         号", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 4, 1));
            table.addCell(createCell(invoice.getPayer().getAccount(), Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, 10, 1));

            table.addCell(createCell("开户银行", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 4, 1));
            table.addCell(createCell(invoice.getPayer().getBank(), Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, 10, 1));

            table.addCell(createCell("开户银行", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 4, 1));
            table.addCell(createCell(invoice.getPayee().getBank(), Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, 10, 1));

            table.addCell(createCell("收入项目", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 10, 1));
            table.addCell(createCell("编码", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 5, 1));
            table.addCell(createCell("数量", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 5, 1));
            table.addCell(createCell("收缴标准", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 5, 1));
            table.addCell(createCell("金额", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 5, 1));

            Item[] itemsArr = new Item[3];
            System.arraycopy(invoice.getItems().toArray(new Item[0]), 0, itemsArr, 0, invoice.getItems().size());
            for (Item item : itemsArr) {
                item = Optional.ofNullable(item).orElse(new Item());
                table.addCell(createCell(item.getValue(), Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 10, 1));
                table.addCell(createCell(item.getCode(), Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 5, 1));
                table.addCell(createCell(item.getNumStr(), Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 5, 1));
                table.addCell(createCell(item.getNorm(), Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 5, 1));
                table.addCell(createCell(item.getAmountStr(), Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, 5, 1));
            }

            String upper = invoice.getAmountUpper();
            String f = "金额(大写)    %s%" + (70 - upper.length()) + "s(小写)    %s";
            String text = String.format(f, upper, '\u0020', invoice.getAmountLower());
            table.addCell(createCell(text , Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, 30, 1));

            text = "执收单位(盖章)\n\n\n\n\n                                 经办人(签章)";
            table.addCell(createCell(text, Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, 15, 1));
            table.addCell(createCell("备注:", Element.ALIGN_TOP, Element.ALIGN_LEFT, 15, 1));

            document.add(table);
            document.close();
        } catch (Exception e) {
            log.error(e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * Create file file.
     *
     * @return the file
     */
    public File createFile() {
        String path = USER_HOME + "/Desktop/ticket_" + System.currentTimeMillis() + ".pdf";
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                // log
                log.info("文件创建成功!");
            } else {
                log.info("文件已存在!");
            }
        } catch (IOException e) {
            // no log
            log.error("create file failed!", e);
        }
        return file;
    }
}
