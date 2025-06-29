package ru.dgritsenko.userinterface.graphical;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.dgritsenko.bank.BankService;
import ru.dgritsenko.userinterface.UserInterface;

public class GraphicalUIService extends Application implements UserInterface {
    private BankService bankService;

    public GraphicalUIService() {
        super();
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.show();
    }

    @Override
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }
}
