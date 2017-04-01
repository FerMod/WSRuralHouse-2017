package examples;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


public class TestTagButton {

    public static void main(String[] args) {
        new TestTagButton();
    }

    public TestTagButton() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JPanel wordsPane = new JPanel(new BorderLayout());
                wordsPane.setBackground(Color.WHITE);

                Word word = new Word("Hand");
                word.addTag("body parts", 55);
                List<String> translations = new ArrayList<>(3);
                translations.add("Reka");
                translations.add("Dion");
                translations.add("Garsec");

                wordsPane.add(new WordGroupPane(word, translations));

                word = new Word("Roof");
                word.addTag("house", 17);
                word.addTag("architecture", 8);
                translations = new ArrayList<>(3);
                translations.add("Dach");
                translations.add("Zadaszenie");

                wordsPane.add(new WordGroupPane(word, translations));

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
//                frame.add(new JScrollPane(wordsPane));
                frame.add(wordsPane);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static class WordGroupPane extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = -9174218515279768280L;

		public WordGroupPane(Word word, List<String> translations) {
            setOpaque(false);
            setLayout(new GridLayout(0, 2));

            add(new WordPane(word));
            add(new TranslationsPane(translations));
        }

    }

    public static class TranslationsPane extends JPanel {

		private static final long serialVersionUID = -3132852526110463326L;
		
		protected static final Border SPLIT_BORDER = new MatteBorder(0, 0, 1, 0, Color.GRAY);

        public TranslationsPane(List<String> translations) {
            setOpaque(false);
            setLayout(new GridLayout(0, 1));
            for (String translation : translations) {
                JLabel lbl = new JLabel(translation);
                lbl.setHorizontalAlignment(JLabel.LEFT);
                lbl.setBorder(SPLIT_BORDER);
                add(lbl);
            }
        }

    }

    public static class WordPane extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = -4310625784786284612L;

		public WordPane(Word word) {
            setBorder(new MatteBorder(0, 0, 1, 1, Color.GRAY));
            setOpaque(false);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.insets = new Insets(10, 8, 10, 8);
            JLabel label = new JLabel(word.getWord());
            add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 0, 0);
            JPanel tagsPane = new JPanel();
            tagsPane.setOpaque(false);
            for (Tag tag : word.getTags()) {
                TagButton tb = new TagButton(tag.getText());
                Font font = tb.getFont();
                font = font.deriveFont(font.getSize() - 3f);
                tb.setFont(font);
                tb.setTag(Integer.toString(tag.getCount()));
                tb.setSelected(true);
                tagsPane.add(tb);
            }
            add(tagsPane, gbc);
        }

    }

    public class Word {

        private String word;
        private List<Tag> tags;

        public Word(String word) {
            this.word = word;
            this.tags = new ArrayList<>(25);
        }

        public void addTag(String text, int count) {
            addTag(new Tag(text, count));
        }

        public String getWord() {
            return word;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void addTag(Tag tag) {
            tags.add(tag);
        }

    }

    public class Tag {

        private String text;
        private int count;

        public Tag(String text, int count) {
            this.text = text;
            this.count = count;
        }

        public String getText() {
            return text;
        }

        public int getCount() {
            return count;
        }

    }

    public static class TagButton extends AbstractButton {

        /**
		 * 
		 */
		private static final long serialVersionUID = -6449364233453211768L;
		private JLabel renderer;
        private String tag;

        public TagButton(String text) {
            this();
            setText(text);
        }

        public TagButton() {
            setModel(new DefaultButtonModel());
            setMargin(new Insets(8, 8, 8, 8));
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    getModel().setPressed(true);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    getModel().setPressed(false);
                    getModel().setSelected(!isSelected());
                }

            });
            setFont(UIManager.getFont("Button.font"));
        }

        public void setTag(String value) {
            if (tag == null ? value != null : !tag.equals(value)) {

                String old = tag;
                tag = value;
                firePropertyChange("tag", old, tag);
                revalidate();

            }
        }

        public String getTag() {
            return tag;
        }

        protected JLabel getRenderer() {

            if (renderer == null) {

                renderer = new JLabel(getText());

            }

            return renderer;

        }

        @Override
        public Dimension getPreferredSize() {

            Insets margin = getMargin();
            Dimension size = new Dimension();
            size.width = margin.left + margin.right;
            size.height = margin.top + margin.bottom;

            JLabel renderer = getRenderer();
            renderer.setText(getText());
            size.width += renderer.getPreferredSize().width;
            size.height += renderer.getPreferredSize().height;

            size.width += getTagWidth();

            return size;
        }

        protected int getTagWidth() {

            JLabel renderer = getRenderer();
            renderer.setText(getTag());
            renderer.setFont(getFont());

            return renderer.getPreferredSize().width + 16;

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

//            int fullWidth = getTagWidth() + 8;
            int width = getTagWidth() + 8;
            int height = getHeight() - 3;
            int tagWidth = getWidth() - 1 - width;

            Shape insert = new TagInsert(width, height);
            int x = getWidth() - width - 1;
            if (!isSelected()) {
                x -= getTagWidth();
            }

            x -= 4;

            g2d.translate(x, 1);
            g2d.setPaint(new Color(242, 95, 0));
            g2d.fill(insert);
            g2d.setPaint(new Color(222, 83, 0));
            g2d.draw(insert);

            Stroke stroke = g2d.getStroke();
            BasicStroke stitch = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{3f}, 0f);
            g2d.setStroke(stitch);
            g2d.setColor(new Color(167, 65, 1));
            g2d.drawLine(0, 2, width, 2);
            g2d.drawLine(0, height - 2, width, height - 2);
            g2d.setColor(new Color(249, 127, 50));
            g2d.drawLine(0, 3, width, 3);
            g2d.drawLine(0, height - 1, width, height - 1);
            g2d.setStroke(stroke);

            if (isSelected()) {

                JLabel renderer = getRenderer();
                renderer.setFont(getFont());
                renderer.setText(getTag());
                renderer.setSize(width - 8, renderer.getPreferredSize().height);
                renderer.setForeground(Color.WHITE);
                renderer.setHorizontalAlignment(JLabel.CENTER);
                int xPos = 4;//((tagWidth - renderer.getWidth()) / 2);
                int yPos = (height - renderer.getHeight()) / 2;
                g2d.translate(xPos, yPos);
                renderer.printAll(g2d);
                g2d.translate(-xPos, -yPos);

            }

            g2d.translate(-x, -1);

            height = getHeight() - 1;

            Shape baseShape = new TagShape(tagWidth, height);

            LinearGradientPaint lgpFill = new LinearGradientPaint(
                            new Point(0, 0),
                            new Point(0, getHeight() - 1),
                            new float[]{0f, 1f},
                            new Color[]{new Color(248, 248, 248), new Color(241, 241, 241)}
            );
            g2d.setPaint(lgpFill);
            g2d.fill(baseShape);

            LinearGradientPaint lgpOutline = new LinearGradientPaint(
                            new Point(0, 0),
                            new Point(0, getHeight() - 1),
                            new float[]{0f, 1f},
                            new Color[]{UIManager.getColor("Button.shadow"), UIManager.getColor("Button.darkShadow")}
            );
            g2d.setPaint(lgpOutline);
            g2d.draw(baseShape);

            JLabel renderer = getRenderer();
            renderer.setFont(getFont());
            renderer.setText(getText());
            renderer.setSize(renderer.getPreferredSize());
            renderer.setForeground(getForeground());

            x = (tagWidth - renderer.getWidth()) / 2;
            int y = (height - renderer.getHeight()) / 2;
            renderer.setLocation(x, y);
            g2d.translate(x, y);
            renderer.printAll(g2d);
            g2d.translate(-x, -y);

            g2d.setColor(Color.RED);
//            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            g2d.dispose();
        }
    }

    protected static class TagInsert extends Path2D.Float {

        /**
		 * 
		 */
		private static final long serialVersionUID = -5871898300058667047L;

		public TagInsert(float width, float height) {

            float gap = 3;

            float radius = (height - (gap * 5)) / 4f;
            moveTo(0, 0);
            lineTo(width, 0);

            @SuppressWarnings("unused")
			float yPos = 0;
            lineTo(width, 1);
            float topY = gap;
            for (int index = 0; index < 4; index++) {

                float bottomY = topY + radius;
                float x = width - (radius / 2);

                lineTo(width, topY);
                curveTo(x, topY, x, bottomY, width, bottomY);
                topY += radius;
                topY += gap;
                lineTo(width, topY);

            }

            lineTo(width, height);
            lineTo(0, height);
            lineTo(0, 0);

        }

    }

    protected static class TagShape extends Path2D.Float {

        /**
		 * 
		 */
		private static final long serialVersionUID = -2294432579842895094L;
		protected static final float RADIUS = 8;

        public TagShape(float width, float height) {

            moveTo(RADIUS, 0);
            lineTo(width, 0);

            float clip = RADIUS / 2f;
            float topY = (height / 2f) - clip;
            float bottomY = (height / 2f) + clip;
            lineTo(width, topY);
            curveTo(width - clip, topY, width - clip, bottomY, width, bottomY);
            lineTo(width, height);
            lineTo(RADIUS, height);

            curveTo(0, height, 0, height, 0, height - RADIUS);
            lineTo(0, RADIUS);
            curveTo(0, 0, 0, 0, RADIUS, 0);

        }

    }
}