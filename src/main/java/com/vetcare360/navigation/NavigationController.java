package com.vetcare360.navigation;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls navigation between different screens in the application
 */
public class NavigationController {
    private static final NavigationController instance = new NavigationController();
    
    private Stage mainStage;
    private StackPane contentArea;
    private final Map<String, String> screens = new HashMap<>();
    private final Map<String, Object> screenData = new HashMap<>();
    
    private NavigationController() {
        // Set up screen mappings
        screens.put("welcome", "/fxml/WelcomeView.fxml");
        screens.put("vets", "/fxml/VeterinarianListView.fxml");
        screens.put("ownerSearch", "/fxml/OwnerSearchView.fxml");
        screens.put("ownerAdd", "/fxml/OwnerAddView.fxml");
        screens.put("ownerEdit", "/fxml/OwnerEditView.fxml");
        screens.put("ownerDetails", "/fxml/OwnerDetailsView.fxml");
        screens.put("ownerList", "/fxml/OwnerListView.fxml");
        screens.put("petAdd", "/fxml/PetAddView.fxml");
        screens.put("petEdit", "/fxml/PetEditView.fxml");
        screens.put("visitAdd", "/fxml/VisitAddView.fxml");
    }
    
    public static NavigationController getInstance() {
        return instance;
    }
    
    public void initialize(Stage stage, FXMLLoader mainLoader) {
        this.mainStage = stage;
        this.contentArea = (StackPane) mainLoader.getNamespace().get("contentArea");
    }
    
    /**
     * Navigate to a specific screen
     * @param screenName the name of the screen to navigate to
     */
    public void navigateTo(String screenName) {
        try {
            if (!screens.containsKey(screenName)) {
                System.err.println("Screen not found: " + screenName);
                return;
            }
            
            String fxmlPath = screens.get(screenName);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            // Apply fade transition
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), view);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            fadeIn.play();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Navigate to a screen and pass data to it
     * @param screenName the name of the screen to navigate to
     * @param key the key for the data
     * @param data the data to pass
     */
    public void navigateTo(String screenName, String key, Object data) {
        screenData.put(key, data);
        navigateTo(screenName);
    }
    
    /**
     * Get data passed during navigation
     * @param key the key for the data
     * @return the data or null if not found
     */
    public Object getData(String key) {
        return screenData.get(key);
    }
    
    /**
     * Clear data after it's been used
     * @param key the key for the data to clear
     */
    public void clearData(String key) {
        screenData.remove(key);
    }
    
    public Stage getMainStage() {
        return mainStage;
    }
}