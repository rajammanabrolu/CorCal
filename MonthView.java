public class MonthView implements JPanel{
    private User user;
    private JTable month;

    public MonthView(User user){
        super(new GridBagLayout());
        this.user=user;
    }

}
