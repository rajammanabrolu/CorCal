public class CompareCal{

    public void createBitField(Calendar tStamp1, Calendar tStamp2, User[] users) {

        int finalDiff = tStamp1.DAY_OF_YEAR - tStamp2.DAY_OF_YEAR;
        int[] field = new int[48*finalDiff];
        
        for (int i = 0; i < 48*finalDiff; ++i) {
            field[i] = 0;
        }

        for (int i = 0; i < users.length ++i) {
            for (int j = 0; j < user[i].events.length; ++j) {
                if (users[i].events[j].DAY_OF_YEAR != NULL && users[i].events[j].HOUR != NULL) {
                    field[user[i].events[j].DAY_OF_YEAR * 48 + user[i].events[j].HOUR * 2] = 1;
                }
            }
        }
    }

    public int[] CompareCal(int[] bitField, int freeHours) {
        int counter = 0;
        int flag = 0;
        int timeDiff = freeHours * 2;
        for(int i = 0; i < bitField.length; ++i) {
            for(int j = 0; j < freeHours * 2; ++j) {
                if(bitField[i + j] == bitField[i + j + 1]) {
                    counter++;
                    if(counter == timeDiff + 2) {
                        endTime = i + j;
                        startTime = i + j - timeDiff;
                        flag = 1;
                    }
                }
            }
        }
        if(flag == 0) {
            for(int i = 0; i < bitField.length; ++i) {
                for(int j = 0; j < freeHours * 2; ++j) {
                    if(bitField[i + j] == bitField[i + j + 1]) {
                        counter++;
                        if(counter == timeDiff + 2) {
                            endTime = i + j + 1;
                            startTime = i + j - timeDiff - 1;
                            flag = 1;
                        }
                    }
                }
            }
        }
        if(flag == 0) {
            endTime = startTime = 0;
        }
        int[] a = {startTime , endTime};
        return a;
}




    }


}
