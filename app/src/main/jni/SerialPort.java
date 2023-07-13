public class SerialPort {
    static {
        System.loadLibrary("serialport"); // JNI kütüphanesini yükleyin
    }

    public native boolean openPort(String portName);
    public native byte[] readData();
    public native void closePort();

    public static void main(String[] args) {
        SerialPort serialPort = new SerialPort();
        boolean opened = serialPort.openPort("/dev/ttyUSB0"); // Seri port yolunu güncelleyin
        if (opened) {
            byte[] data = serialPort.readData();
            if (data != null) {
                String receivedData = new String(data);
                System.out.println("Received data: " + receivedData);
            }
            serialPort.closePort();
        }
    }
}
