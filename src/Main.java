import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        String allImagesDir = "D:\\BeQuynh\\Origin";
        String selectedImagesDir = "D:\\BeQuynh\\Old";
        String remainingImagesDir = "D:\\BeQuynh\\New";

        File allImagesFolder = new File(allImagesDir);
        File selectedImagesFolder = new File(selectedImagesDir);

        File[] allImages = allImagesFolder.listFiles();
        File[] selectedImages = selectedImagesFolder.listFiles();

        if (allImages == null || selectedImages == null) {
            System.out.println("Folder not found!");
            return;
        }

        Set<String> selectedImageHashes = new HashSet<>();
        for (File file : selectedImages) {
            try {
                selectedImageHashes.add(getFileChecksum(file));
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        // Tạo thư mục đích nếu chưa có
        File remainingFolder = new File(remainingImagesDir);
        if (!remainingFolder.exists()) {
            remainingFolder.mkdir();
        }

        int count = 0;
        for (File file : allImages) {
            try {
                String hash = getFileChecksum(file);
                if (!selectedImageHashes.contains(hash) && count < 466) {
                    Files.copy(file.toPath(),
                            Path.of(remainingImagesDir, file.getName()),
                            StandardCopyOption.REPLACE_EXISTING);
                    count++;
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Copy " + count + " remain picture successfully!");
    }

    // Hàm tính toán mã băm SHA-256 cho file
    private static String getFileChecksum(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }

        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}