package Main.Home.Logic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import Main.Authentication.Logic.FileManager;
import Main.Authentication.Logic.LoginController;
import Main.Authentication.Model.User;
import Main.Main;
import Main.InventoryHelper.IAdapter;
import Main.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/*
    Logic implementation for the user home page.
 */
public class UserHomeController implements IAdapter {

    private FileManager io = new FileManager();

    @FXML
    public Label unameField;
    public Button borrowedBtn;
    public Button viewCatalogBtn;

    @Override
    public void init() {
        if(Statics.CurrentUser!=null) {
            System.out.println("CURRENT USER:"+ Statics.CurrentUser);
            loadUsers();
            unameField.setText(Statics.CurrentUser.getUsername()+"("+Statics.CurrentUser.getType().name().toLowerCase()+")");
            try{
                User user = Statics.Users.stream().filter((user1)-> user1.getId().equals(Statics.CurrentUser.getId())).collect(Collectors.toList()).get(0);
                Statics.CurrentUser = user;
                ArrayList<User> users= new ArrayList<>();
                users.add(Statics.CurrentUser);
                io.serializeToFile("currentUser.ser",users);
            }catch(ArrayIndexOutOfBoundsException exc){
                System.out.println("An Array Out of Bounds exception has occured in init.UserHomeController.java: This is most likely because the current user id no longer exists among the lists of users!\nAdvised: Delete Current user");
            }


        }else{
            System.out.println("CURRENT USER:"+ Statics.CurrentUser);
        }
    }

    @Override
    public void custom(Object... args) {
    }

    private void loadUsers() {
        Arrays.asList("AdminDB.ser","CustomerDB.ser").forEach(path-> {
            try {
                System.out.println(path);
                io.readSerializedFile((String)path,"users");
                Statics.Users.addAll(io.users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /*
        Settings icon clickable which currently leads back to login page
     */
    public void onSettings(MouseEvent mouseEvent) {
        ArrayList<User> users= new ArrayList<>();
        new FileManager().serializeToFile("currentUser.ser",users);
        try {
            Main.currentStage.setFXMLScene("Authentication/UI/login.fxml",new LoginController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        View catalog button
     */
    public void viewCatalog(ActionEvent actionEvent) {
        try {
            Main.currentStage.setFXMLScene("Home/UI/catalog.fxml", new ViewCatalogController());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
        View borrowed machines button
     */
    public void viewBorrowedMachines(ActionEvent actionEvent) {
        try {
            Main.currentStage.setFXMLScene("Home/UI/borrowedItems.fxml", new BorrowedItemsController());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
