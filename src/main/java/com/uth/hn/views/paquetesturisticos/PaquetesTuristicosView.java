package com.uth.hn.views.paquetesturisticos;

import com.uth.hn.controller.PaqueteaTuristicosInteractor;
import com.uth.hn.controller.PaquetesTuristicosInteractorImpl;
import com.uth.hn.data.PaquetesTuristicos;
import com.uth.hn.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("PaquetesTuristicos")
@Route(value = "paquetesTuristicos/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class PaquetesTuristicosView extends Div implements BeforeEnterObserver,PaquetesTuristicosViewModel {

    private final String SAMPLEPERSON_ID = "samplePersonID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "paquetesTuristicos/%s/edit";

    private final Grid<PaquetesTuristicos> grid = new Grid<>(PaquetesTuristicos.class, false);

    private TextField nombrePaquete;
    private TextField destino;
    private NumberField precio;
    private TextField descripcion;
    private IntegerField duracion;
    private IntegerField cupoPersonas;
   // private Checkbox important;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");
    private final Button delete = new Button("Eliminar", new Icon(VaadinIcon.TRASH));

    private PaquetesTuristicos paquetesTuristicos;
    private PaqueteaTuristicosInteractor controlador;
    private List<PaquetesTuristicos> elementos;
    

    public PaquetesTuristicosView( ) {
        addClassNames("paquetes-turisticos-view");
        
        controlador = new PaquetesTuristicosInteractorImpl(this);
        this.elementos = new ArrayList<>();
        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        //grid.addColumn("idPaquete").setAutoWidth(true).setHeader("Numero de Paquete");
        grid.addColumn("nombrePaquete").setAutoWidth(true).setHeader("Nombre");
        grid.addColumn("destino").setAutoWidth(true).setHeader("Destino");
        grid.addColumn("precio").setAutoWidth(true).setHeader("Precio");
        grid.addColumn("descripcion").setAutoWidth(true).setHeader("Descripcion");
        grid.addColumn("duracion").setAutoWidth(true).setHeader("Duracion dse noches");
        /*LitRenderer<PaquetesTuristicos> importantRenderer = LitRenderer.<PaquetesTuristicos>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", important -> important.isImportant() ? "check" : "minus").withProperty("color",
                        important -> important.isImportant()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");*/

        //grid.addColumn(importantRenderer).setHeader("cupoPersonas").setAutoWidth(true).setHeader("Cupo de Personas");
        grid.addColumn("cupoPersonas").setAutoWidth(true).setHeader("Cupo de Personas");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getIdPaquete()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PaquetesTuristicosView.class);
            }
        });

        // Configure Form

        // Bind fields. This is where you'd define e.g. validation rules

        controlador.consultarPaquetesTuristicos();
        
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.paquetesTuristicos == null) {
                    this.paquetesTuristicos = new PaquetesTuristicos();
                }
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(PaquetesTuristicosView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } 
        });
        
    delete.addClickListener( e -> {
        	
        });
        
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> paquetesId = event.getRouteParameters().get(SAMPLEPERSON_ID)
            .filter(s -> !s.equals("null"))  // Filtra el valor "null"
            .map(Integer::parseInt);

        if (paquetesId.isPresent()) {
            PaquetesTuristicos paquetesTuristicosFromBackend = obtenerPaquetesTuristicos(paquetesId.get());
            if (paquetesTuristicosFromBackend != null) {
                populateForm(paquetesTuristicosFromBackend);
            } else {
                Notification.show(
                    String.format("El paquete turístico solicitado no se encontró, ID = %s", paquetesId.get()), 
                    3000, Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(PaquetesTuristicosView.class);
            }
        } else {
            Notification.show("Identificador de paquete turístico no válido", 3000, Notification.Position.BOTTOM_START);
            refreshGrid();
            event.forwardTo(PaquetesTuristicosView.class);
        }
    }


    private PaquetesTuristicos obtenerPaquetesTuristicos(Integer paquetesId) {
    	PaquetesTuristicos paquetesTuristicosEncontrado = null;
		for (PaquetesTuristicos paquetesTuristicos : elementos) {
			if(paquetesTuristicos.getIdPaquete() == paquetesId) {
				paquetesTuristicosEncontrado = paquetesTuristicos;
				break;
			}
		}
		return paquetesTuristicosEncontrado;
	}
    
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

     // ...

        FormLayout formLayout = new FormLayout();

        nombrePaquete = new TextField("Nombre del Paquete");
        nombrePaquete.setPrefixComponent(VaadinIcon.FOLDER_ADD.create());

        destino = new TextField("Destino");
        destino.setPrefixComponent(VaadinIcon.AIRPLANE.create());

        precio = new NumberField();
        precio.setLabel("Precio");
        precio.setValue(0.0);  // Asigna un valor Double
        Div dollarPrefix = new Div();
        dollarPrefix.setText("$");
        precio.setPrefixComponent(dollarPrefix);
        add(precio);

        descripcion = new TextField("Descripcion");

        duracion = new IntegerField();
        duracion.setLabel("Duracion por noches");
        duracion.setMin(0);
        duracion.setMax(10);
        duracion.setValue(2);  // Asigna un valor Integer
        duracion.setStepButtonsVisible(true);
        add(duracion);


        cupoPersonas = new IntegerField();
        cupoPersonas.setLabel("Cupo maximo de Personas");
        cupoPersonas.setMin(0);
        cupoPersonas.setMax(10);
        cupoPersonas.setValue(0);  // Asigna un valor Integer
        cupoPersonas.setStepButtonsVisible(true);
        add(cupoPersonas);
        
        formLayout.add(nombrePaquete, destino, precio, descripcion, duracion, cupoPersonas);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        // ...



        splitLayout.addToSecondary(editorLayoutDiv);
    }
    
    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(PaquetesTuristicos value) {
        this.paquetesTuristicos = value;
        
        if(value == null) {
        	this.nombrePaquete.setValue("");
            this.destino.setValue("");
            this.precio.setValue("");
            this.descripcion.setValue("");
            this.duracion.setValue("");
            this.cupoPersonas.setValue("");
        }else {
        this.nombrePaquete.setValue(value.getNombrePaquete());
        this.destino.setValue(value.getDestino());
        this.precio.setValue(value.getPrecio());
        this.descripcion.setValue(value.getDescripcion());
        this.duracion.setValue(value.getDuracion());
        this.cupoPersonas.setValue(value.getCupoPersonas());
    }

	@Override
	public void mostarPaquetesTuristicosEnGrid(List<PaquetesTuristicos> items) {
		Collection<PaquetesTuristicos> ItemsCollection = items;
		grid.setItems(ItemsCollection);
		this.elementos = items;
	}
}