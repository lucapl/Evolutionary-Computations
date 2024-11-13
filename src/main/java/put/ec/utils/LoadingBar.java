package put.ec.utils;

public class LoadingBar {
    private final int total;
    private final int length;
    private final String message;

    public LoadingBar(int total, String message){
        this.total = total-1;
        this.length = 20;
        this.message = message;
    }

    public void progress(int value){
        System.out.print("\r"+message+": [");
        int percent = (value * 100) / total;

        // Print the progress bar
        int barPercent = (int) (((double)percent)/100 * length);
        for(int i = 0; i<length;i++) {
            if(i<barPercent) {
                System.out.print("=");
            }else{
                System.out.print("-");
            }
        }

        // Print percentage on the same line
        System.out.print("] " + percent + "%");

        if(value == total){
            System.out.println(" Done!");
        }
    }
}
