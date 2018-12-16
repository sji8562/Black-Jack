/* Game.java
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game extends JFrame implements ActionListener {

    private Deck deck;
    public Hogu hogu = new Hogu("Hogu");
    public Hogu Banker = new Hogu("Banker");

    private JButton Hit_btn = new JButton("Hit");
    private JButton Stay_btn = new JButton("Stay");
    private JButton Deal_btn = new JButton("Deal");

    private JLabel Status_label = new JLabel(" ", JLabel.CENTER);

    JPanel HoguPanel = new JPanel();
    JPanel BankerPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JPanel statusPanel = new JPanel();
    
    Game() {
        JFrame gameFrame = new JFrame("We ProjectIc,Hong");
        gameFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("cards/10.png"));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        

        buttonsPanel.add(Hit_btn);
        buttonsPanel.add(Stay_btn);
        buttonsPanel.add(Deal_btn);
        statusPanel.add(Status_label);

        Hit_btn.addActionListener(this);
        Stay_btn.addActionListener(this);
        Deal_btn.addActionListener(this);

        Hit_btn.setEnabled(false);
        Stay_btn.setEnabled(false);

        BankerPanel.setBackground(Color.GRAY);
        HoguPanel.setBackground(Color.GRAY);
        buttonsPanel.setBackground(Color.GRAY);
        statusPanel.setBackground(Color.GRAY);

        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(BankerPanel, BorderLayout.NORTH);
        gameFrame.add(HoguPanel, BorderLayout.CENTER);
        gameFrame.add(buttonsPanel, BorderLayout.SOUTH);
        gameFrame.add(statusPanel, BorderLayout.WEST);
        gameFrame.repaint();
        gameFrame.setSize(450, 350);
        gameFrame.setVisible(true);
    }


    private void hitHogu() {
        Card newCard = hogu.dealTo(deck.dealFrom());
        HoguPanel.add(new JLabel(new ImageIcon("cards/" + newCard.toString())));
        HoguPanel.updateUI();
    }

    private void hitBankerDown() {
        Card newCard = Banker.dealTo(deck.dealFrom());
        BankerPanel.add(new JLabel(new ImageIcon("cards/b2fv.png")));
        BankerPanel.updateUI();
    }

    private void hitBanker() {
        Card newCard = Banker.dealTo(deck.dealFrom());
        BankerPanel.add(new JLabel(new ImageIcon("cards/" + newCard.toString())));
        BankerPanel.updateUI();
    }

    private void deal() {
        HoguPanel.removeAll();
        BankerPanel.removeAll();
        HoguPanel.updateUI();
        BankerPanel.updateUI();
        hogu.reset();
        Banker.reset();
        if (deck == null || deck.size() < 15) {
            deck = new Deck();
            deck.shuffle();
            Status_label.setText("Shuffling");
        }
        hitHogu();
        hitBankerDown();
        hitHogu();
        hitBanker();
    }

    private void checkWinner() {
        BankerPanel.removeAll();
        for (int i = 0; i < Banker.inHand(); i++) {
            BankerPanel.add(new JLabel(new ImageIcon("cards/" + Banker.cards[i].toString())));
        }
        if (hogu.value() > 21) {
            Status_label.setText("Hogu Busts");
        } else if (Banker.value() > 21) {
            Status_label.setText("Banker Busts");
        } else if (Banker.value() == hogu.value()) {
            Status_label.setText("Push");
        } else if (Banker.value() < hogu.value()) {
            Status_label.setText("Hogu Wins");
        } else {
            Status_label.setText("Banker Wins");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Hit_btn) {
            hitHogu();
            if (hogu.value() > 21) {
                checkWinner();
                Hit_btn.setEnabled(false);
                Stay_btn.setEnabled(false);
                Deal_btn.setEnabled(true);
            }
        }

        if (e.getSource() == Stay_btn) {
            while (Banker.value() < 17 || hogu.value() > Banker.value()) {
                hitBanker();
            }
            checkWinner();
            Hit_btn.setEnabled(false);
            Stay_btn.setEnabled(false);
            Deal_btn.setEnabled(true);
        }

        if (e.getSource() == Deal_btn) {
            deal();
            Status_label.setText(" ");
            Hit_btn.setEnabled(true);
            Stay_btn.setEnabled(true);
            Deal_btn.setEnabled(false);
        }
    }

    public static void main(String[] args) throws IOException {
        new Game();
    }
}
