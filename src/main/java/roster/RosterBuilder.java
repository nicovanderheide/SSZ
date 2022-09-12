package roster;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.BackgroundImage;
import com.itextpdf.layout.properties.BackgroundRepeat;
import com.itextpdf.layout.properties.BackgroundSize;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import data.Crew;
import data.Member;
import parser.CrewReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class RosterBuilder {
    private static Image topImage;
    private static BackgroundImage.Builder backgroundImageBuilder;

    static {
        topImage = new Image(ImageDataFactory.create(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("Top.png"))));
        Image memberImage = new Image(ImageDataFactory.create(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("member.png"))));

        backgroundImageBuilder = new BackgroundImage.Builder();
        backgroundImageBuilder.setBackgroundRepeat(new BackgroundRepeat(BackgroundRepeat.BackgroundRepeatValue.NO_REPEAT));
        BackgroundSize backgroundSize = new BackgroundSize();
        backgroundSize.setBackgroundSizeToValues(UnitValue.createPercentValue(100), UnitValue.createPercentValue(100));
        backgroundImageBuilder.setBackgroundSize(backgroundSize);
        backgroundImageBuilder.setImage(memberImage.getXObject());
    }


    public static void main(final String... args) {
        RosterBuilder rb = new RosterBuilder();
        rb.create(args[0]);

    }

    public void create(final String crewFile) {
        try (final PdfDocument pdf = new PdfDocument(new PdfWriter(new File("crew.pdf")));
             final Document document = new Document(pdf, PageSize.A4)
        ) {

            Crew crew = CrewReader.read(crewFile);
            Table table = new Table(1).useAllAvailableWidth();
            table.setBorder(Border.NO_BORDER);
            table.setMinWidth(UnitValue.createPercentValue(100));

            table.addCell(createTopCell(crew));
            table.startNewRow();

            Iterator<Member> iterator = crew.getMembers().iterator();

            table.addCell(addMemberCell(iterator.next()));
            table.addCell(addMemberCell(iterator.next()));
            table.addCell(addMemberCell(iterator.next()));
            table.addCell(addMemberCell(iterator.next()));

            document.add(table);
            if (crew.getMembers().size() > 4) {
                document.add(new AreaBreak());

                Table extraTable = new Table(1).useAllAvailableWidth();
                extraTable.setBorder(Border.NO_BORDER);
                extraTable.setMinWidth(UnitValue.createPercentValue(100));

                extraTable.addCell(addMemberCell(iterator.next()));
                extraTable.addCell(addMemberCell(iterator.next()));

                extraTable.addCell(addMemberCell(iterator.next()));
                extraTable.addCell(addMemberCell(iterator.next()));

                document.add(extraTable);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Cell createTopCell(final Crew crew) {
        Cell topCell = new Cell();
        topCell.setBorder(Border.NO_BORDER);
        topCell.setHeight(240);
        BackgroundSize backgroundSize = new BackgroundSize();
        backgroundSize.setBackgroundSizeToValues(UnitValue.createPercentValue(100), UnitValue.createPercentValue(100));
        BackgroundImage.Builder backgroundImageBuilder = new BackgroundImage.Builder();
        backgroundImageBuilder.setBackgroundRepeat(new BackgroundRepeat(BackgroundRepeat.BackgroundRepeatValue.NO_REPEAT));
        backgroundImageBuilder.setBackgroundSize(backgroundSize);
        backgroundImageBuilder.setImage(topImage.getXObject());
        topCell.setBackgroundImage(backgroundImageBuilder.build());

        topCell.add(new Paragraph(crew.getType()).setRelativePosition(75, 0, 0, 0));
        topCell.add(new Paragraph(crew.getShip()).setRelativePosition(250, -20, 0, 0));
        topCell.add(new Paragraph(crew.getAgenda()).setRelativePosition(110, -10, 0, 0));
        topCell.add(new Paragraph(crew.getEdge()).setRelativePosition(33, 0, 0, 0));

        topCell.add(new Paragraph("").setHeight(20));
        topCell.add(new Paragraph(crew.getCommander().getName()).setRelativePosition(70, 0, 0, 0));

        topCell.add(new Paragraph("").setHeight(7));
        Table stats = new Table(UnitValue.createPointArray(new float[]{25, 20, 36, 20, 35, 20, 35, 20, 35, 20, 51, 20, 70, 100}));
        stats.setBorder(Border.NO_BORDER);
        stats.setFixedLayout();
        stats.addCell(createCell(""));
        stats.addCell(createCell(crew.getCommander().getStats().getLife()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(crew.getCommander().getStats().getMovement()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(crew.getCommander().getStats().getCombat()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(crew.getCommander().getStats().getReaction()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(crew.getCommander().getStats().getIntelligence()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(""));
        stats.addCell(createCell(""));
        stats.addCell(createCell(""));
        topCell.add(stats);

        topCell.add(new Paragraph("").setHeight(15));

        Table cmdrTable = new Table(2);
        cmdrTable.setFixedLayout();
        cmdrTable.setWidth(363);
        Cell abilities = new Cell(1, 1).setBorder(Border.NO_BORDER).setMargin(0);
        for (String ability : crew.getCommander().getAbilities()) {
            abilities.add(new Paragraph(ability));
        }
        cmdrTable.addCell(abilities);

        Cell equipment = new Cell(1, 1).setBorder(Border.NO_BORDER).setMargin(0);
        for (String stuff : crew.getCommander().getEquipment()) {
            equipment.add(new Paragraph(stuff));
        }
        cmdrTable.addCell(equipment);
        topCell.add(cmdrTable);

        return topCell;
    }

    public Cell addMemberCell(final Member member) {

        Cell memberCell = new Cell();
        memberCell.setBorder(Border.NO_BORDER);
        memberCell.setHeight(120);
        memberCell.setBackgroundImage(backgroundImageBuilder.build());

        memberCell.add(addMemberTable(member));
        return memberCell;
    }


    private Table addMemberTable(final Member member) {
        Table memberTable = new Table(UnitValue.createPercentArray(2));
        memberTable.setMargin(0);
        memberTable.useAllAvailableWidth();
        memberTable.setBorder(Border.NO_BORDER);

        memberTable.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(member.getName()).setRelativePosition(70, 2, 0, 0)));
        memberTable.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(member.getType()).setRelativePosition(135, 2, 0, 0)));

        memberTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER).add(new Div()));
        memberTable.startNewRow();

        Table stats = new Table(UnitValue.createPointArray(new float[]{25, 20, 36, 20, 35, 20, 35, 20, 35, 20, 51, 20, 70, 100}));
        stats.setBorder(Border.NO_BORDER);
        stats.setFixedLayout();
        stats.addCell(createCell(""));
        stats.addCell(createCell(member.getStats().getLife()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(member.getStats().getMovement()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(member.getStats().getCombat()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(member.getStats().getReaction()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(member.getStats().getIntelligence()));
        stats.addCell(createCell(""));
        stats.addCell(createCell(""));
        stats.addCell(createCell(""));
        stats.addCell(createCell(""));

        memberTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER).add(stats));

        memberTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER).add(new Div()));
        memberTable.startNewRow();
        memberTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER).add(new Div()));
        memberTable.startNewRow();
        memberTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER).add(new Div()));
        memberTable.startNewRow();
        memberTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER).add(new Div()));
        memberTable.startNewRow();
        memberTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER).add(new Div()));
        memberTable.startNewRow();

        Cell abilities = new Cell(1, 1).setBorder(Border.NO_BORDER).setMargin(0);
        for (String ability : member.getAbilities()) {
            abilities.add(new Paragraph(ability));
        }
        memberTable.addCell(abilities);

        Cell equipment = new Cell(1, 1).setBorder(Border.NO_BORDER).setMargin(0);
        for (String stuff : member.getEquipment()) {
            equipment.add(new Paragraph(stuff));
        }
        memberTable.addCell(equipment);

        return memberTable;
    }

    private Cell createCell(final String text) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER);
        cell.add(new Paragraph(text));
        cell.setTextAlignment(TextAlignment.CENTER);
        return cell;
    }
}
