package examples;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        final DecimalFormat df = new DecimalFormat("0.####");
        final JFrame frame = new JFrame();
        final JTextField text = new JTextField(20);
        final DoubleJSlider slider = new DoubleJSlider(0, 100, 0, 1000);
        slider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                text.setText(df.format(slider.getScaledValue()));
            }
        });
        text.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent ke) {
                String typed = text.getText();
                slider.setValue(0);
                if(!typed.matches("\\d+(\\.\\d*)?")) {
                    return;
                }
                double value = Double.parseDouble(typed)*slider.scale;
                slider.setValue((int)value);
            }
        });
        frame.setLayout(new BorderLayout());
        frame.add(text, BorderLayout.NORTH);
        frame.add(slider, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }
}

class DoubleJSlider extends JSlider {

    final int scale;

    public DoubleJSlider(int min, int max, int value, int scale) {
        super(min, max, value);
        this.scale = scale;
    }

    public double getScaledValue() {
        return ((double)super.getValue()) / this.scale;
    }
}