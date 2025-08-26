public class Main {
    public static void main(String[] args) {
        UserFileManager.loadAllUsers();
        BookFileManager.loadBooks(BookFileManager.getBookFile());
        BookFileManager.loadImagesFromFolder(BookFileManager.getBookImageFile());
        BookFileManager.loadBooks(BookFileManager.getReserveBookFile());  
        BookFileManager.loadBooks(BookFileManager.getBorrowFile());

        new DesktopPage();
    }
}