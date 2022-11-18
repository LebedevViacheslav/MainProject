package com.mygdx.data.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DataBuffer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.data.data.DatabaseTest;

public class MainScreen implements Screen {
    private Stage stage;
    private Viewport viewport = new ScreenViewport();

    private ScrollPane scrollPane;
    private List list;
    private Button button;
    private Label label;
    private TextField textField;

    private Texture listBox;
    private Texture probe;
    private Texture buttonDown;
    private Texture buttonUp;
    private Texture cursorTexture;
    private Texture textInputTexture;

    private TextField.TextFieldStyle style;
    private List.ListStyle listStyle;

    private Array<Array> tableData;

    public MainScreen() {
        stage = new Stage(viewport);

        DatabaseTest databaseTest = new DatabaseTest();
        databaseTest.insertData("Meat", "Meat products");
        databaseTest.insertData("Milk", "Milk products");
        databaseTest.insertData("Fruits", "Fruits products");
        databaseTest.insertData("Greens", "Greens products");
        databaseTest.insertData("Eggs", "Eggs products");
        databaseTest.insertData("Grain", "Grain products");
        databaseTest.insertData("Fish", "Fish products");
        databaseTest.closeDB();

        listBox = new Texture("listBox.png");
        probe = new Texture("probe.png");
        buttonDown = new Texture("buttonDown.png");
        buttonUp = new Texture("buttonUp.png");
        cursorTexture =  new Texture("cursor.png");
        textInputTexture = new Texture("inputText.png");

        style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.CYAN;
        style.background = new TextureRegionDrawable(textInputTexture);
        style.messageFontColor = Color.CYAN;
        style.cursor = new TextureRegionDrawable(cursorTexture);

        listStyle = new List.ListStyle();
        listStyle.background = new TextureRegionDrawable(probe);
        listStyle.selection = new TextureRegionDrawable(listBox);
        listStyle.font = new BitmapFont();
        listStyle.fontColorUnselected = Color.BLUE;
        listStyle.fontColorSelected = Color.CYAN;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.CYAN;

        label = new Label("", labelStyle);

        button = new Button(new TextureRegionDrawable(buttonUp), new TextureRegionDrawable(buttonDown));

        list = new List(listStyle);

        textField = new TextField("", style);


        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = new TextureRegionDrawable(probe);
        scrollPaneStyle.hScrollKnob = new TextureRegionDrawable(probe);

        scrollPane = new ScrollPane(list, scrollPaneStyle);

        int width = Gdx.graphics.getWidth()/2;
        int height = Gdx.graphics.getHeight()/2;

        scrollPane.setPosition(width-50, height-50);
        scrollPane.setSize(200, 100);

        textField.setPosition(width-50, height+70);
        textField.setSize(100, 50);

        button.setPosition(width-50, height - 100);
        button.setSize(100, 50);

        label.setPosition(width-50, height - 120);


        stage.addActor(button);
        stage.addActor(label);
        stage.addActor(scrollPane);
        stage.addActor(textField);
        stage.setDebugAll(false);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getData(textField.getText());
            }
        });

        getData("");
    }

    private void getData(String s) {
        Array<Table> array = new Array<>();
        DatabaseTest databaseTest = new DatabaseTest();
        Table table = new Table();

        tableData = databaseTest.getData(s);
        for (Array rowData : tableData) {
            for (Object cellData : rowData) {
                TextField label = new TextField(cellData.toString(), style);
                label.setTouchable(Touchable.disabled);
                table.add(label).width(50).height(30);
            }
            table.row();
        }

        table.left().top();
        table.setTouchable(Touchable.enabled);

        table.addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showProperties((int)y, 100);
            }
        });

        table.setPosition(0, 0);
        table.setSize(100, 100);

        array.add(table);
        stage.addActor(table);

        databaseTest.closeDB();

        scrollPane.setActor(table);
    }

    public void showProperties(int y, float height){

        int a = tableData.size - (y/30) - 1;

        if (tableData.size * 30 < height) {
            a = (int)((height - y)/30);
        }
        System.out.println(tableData.size < height);
        System.out.println(height);
        System.out.println(a);
        if (a <= tableData.size - 1) {
            Array<String> rowData = tableData.get(a);
            label.setText(rowData.toString());
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        listBox.dispose();
        probe.dispose();
        buttonDown.dispose();
        buttonUp.dispose();
        cursorTexture.dispose();
        textInputTexture.dispose();
    }
}
