package SBUGRAM;

import SBUGRAM.Messages.Handler;
import SBUGRAM.Scenes.Viewer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Tools {
    public static Handler reader(Server server, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        Object result;
        while (true) {
            if ((result = inputStream.readObject()) != null) {
                return (Handler) result;
            }
        }
    }

    public static void Outer(ObjectOutputStream outputStream, Object object) {
        try {
            outputStream.writeUnshared(object);
//            outputStream.writeObject(object);
            outputStream.flush();
            outputStream.reset();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-655);
        }
    }

    public static Object Inner(ObjectInputStream inputStream, long milliSecondWait) {
        sleep(milliSecondWait);
        Object result = null;
        try {
            result = inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ObjectInputStream getInputStream(Server server, String userName) {
        return (ObjectInputStream) server.connections.get(userName).get("in");
    }

    public static ObjectOutputStream getOutStream(Server server, String userName) {
        return (ObjectOutputStream) server.connections.get(userName).get("out");
    }

    public static void sleep(long milliSecond) {
        try {
            Thread.sleep(milliSecond);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sleep2(long milliSecond) {
        long a = System.currentTimeMillis();
        while (true) {
            long b = System.currentTimeMillis();
            if (b - a > milliSecond) break;
        }
    }

    public static List<User> listMaker(ConcurrentHashMap<String, User> map) {
        Set<String > keys = map.keySet();
        List<User> users = new ArrayList<>();
        for (String key : keys) {
            users.add(map.get(key));
        }
        return users;
    }

    public static void printException(Exception exception) {
        try {
            throw exception;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(User from, User to) {
        Field[] fields = from.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (field.getName().equals("inputStream") || field.getName().equals("outputStream") || field.getName().equals("socket") || field.getName().equals("server")) {
                        continue;
                    }
                    field.set(to, field.get(from));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-3);
            }
        }
    }

    public static void viewerOut(Object object) {
        System.out.println("user is " + Viewer.getUser().getUserName());
        Outer(Viewer.getUser().getOutputStream(), object);
    }

    public static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void Time() {
        System.out.println(getTime());
    }

//    public static byte[] imageToByte(String path) {
//        try {
//            BufferedImage bImage = ImageIO.read(new File(path));
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ImageIO.write(bImage, "jpg", bos);
//            return bos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
///*        File imgPath = new File(path);
//        try {
//            BufferedImage bufferedImage = ImageIO.read(imgPath);
//            // get DataBufferBytes from Raster
//            WritableRaster raster = bufferedImage .getRaster();
//            DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
//            return ( data.getData() );
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }*/
//    }

    public static byte[] imageToByte(Image image) {
        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos);
            return bos.toByteArray();
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static Image byteToImage(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "png", new File("output.jpg") );
            return SwingFXUtils.toFXImage(bImage2, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
