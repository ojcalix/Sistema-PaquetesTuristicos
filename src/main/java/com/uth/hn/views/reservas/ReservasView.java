package com.uth.hn.views.reservas;

import com.uth.hn.controller.ReservasInteractor;
import com.uth.hn.controller.ReservasInteractorImpl;
import com.uth.hn.data.Reservas;
import com.uth.hn.views.MainLayout;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import com.vaadin.flow.component.combobox.ComboBox;

@PageTitle("Reservas")
@Route(value = "reservas/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class ReservasView extends Div implements BeforeEnterObserver,ReservasViweModel {

    private final String SAMPLEPERSON_ID = "samplePersonID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "reservas/%s/edit";

    private final Grid<Reservas> grid = new Grid<>(Reservas.class, false);

    private TextField nombrePaquete;
    private TextField destino;
    private DatePicker precio;
    private TextField descripcion;
    private ComboBox<String> fechaInicio;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");
    private final Button delete = new Button("Eliminar", new Icon(VaadinIcon.TRASH));

    private Reservas reservas;
    private ReservasInteractor controlador;
    private List<Reservas> elementos;

    public ReservasView( ) {
        addClassNames("reservas-view");

        controlador = new ReservasInteractorImpl(this);
        this.elementos = new ArrayList<>();
        
        //UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        //avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        //avatarGroup.getStyle().set("visibility", "hidden");*/

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("idReserva").setAutoWidth(true).setHeader("Numero de Reserva");
        grid.addColumn("nombrePaquete").setAutoWidth(true).setHeader("Numero de Paquete");
        grid.addColumn("destino").setAutoWidth(true).setHeader("Numero de Cliente");
        grid.addColumn("precio").setAutoWidth(true).setHeader("Fecha de Reserva");
        grid.addColumn("descripcion").setAutoWidth(true).setHeader("Precio Total");
        grid.addColumn("fechaInicio").setAutoWidth(true).setHeader("Estado");
        /*LitRenderer<Reservas> importantRenderer = LitRenderer.<Reservas>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", important -> important.isImportant() ? "check" : "minus").withProperty("color",
                        important -> important.isImportant()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");*/

        //grid.addColumn(importantRenderer).setHeader("Important").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getIdReserva()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ReservasView.class);
            }
        });
        controlador.consultarReservas();
        // Configure Form
        //binder = new CollaborationBinder<>(Reservas.class, userInfo);

        // Bind fields. This is where you'd define e.g. validation rules


        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.reservas == null) {
                    this.reservas = new Reservas();
                    
                    this.reservas.setNombrePaquete(this.nombrePaquete.getValue());
                    this.reservas.setDestino(this.destino.getValue());
                    this.reservas.setPrecio(this.precio.getValue());
                    this.reservas.setDescripcion(this.descripcion.getValue());
                    this.reservas.setFechaInicio(this.fechaInicio.getValue());
                    
                    this.controlador.crearReservas(reservas);
                }else {
                	
                }
                
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ReservasView.class);
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
        Optional<Integer> reservasId = event.getRouteParameters().get(SAMPLEPERSON_ID).map(Integer::parseInt);
        if (reservasId.isPresent()) {
            Reservas reservasPersonFromBackend = obtenerReservas(reservasId.get());
            if (reservasPersonFromBackend != null) {
                populateForm(reservasPersonFromBackend);
            } else {
                Notification.show(String.format("The requested samplePerson was not found, ID = %d", reservasId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(ReservasView.class);
            }
        }
    }
    private Reservas obtenerReservas(Integer reservasId) {
    	Reservas reservaEncontrado = null;
		for (Reservas reserva : elementos) {
			if(reservas.getIdReserva() == reservasId) {
				reservaEncontrado = reserva;
				break;
			}
		}
		return reservaEncontrado;
	}

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        
        nombrePaquete = new TextField("Numero de Paquete");
        nombrePaquete.setPrefixComponent(VaadinIcon.LEVEL_RIGHT_BOLD.create());
        
        destino = new TextField("Numero de Cliente");
        destino.setPrefixComponent(VaadinIcon.LEVEL_RIGHT_BOLD.create());

        precio = new DatePicker("Fecha de Reserva");

        NumberField descripcion = new NumberField();
        descripcion.setLabel("Precio Total");
        descripcion.setValue(0.0);
        Div dollarPrefix2 = new Div();
        dollarPrefix2.setText("$");
        descripcion.setPrefixComponent(dollarPrefix2);
        
        add(descripcion);
        
        fechaInicio = new ComboBox<>("Estado");
        fechaInicio.setItems("0", "1");
        fechaInicio.setAllowCustomValue(false);
        add( fechaInicio);
        
        formLayout.add( nombrePaquete, destino, precio, descripcion, fechaInicio);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

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

    private void populateForm(Reservas value) {
        this.reservas = value;
        if(value == null) {
        	this.nombrePaquete.setValue("");
            this.destino.setValue("");
            //this.precio.setValue("");
            this.descripcion.setValue("");
            this.fechaInicio.setValue("");
        }else {
        	this.nombrePaquete.setValue(value.getNombrePaquete());
            this.destino.setValue(value.getDestino());
            this.precio.setValue(value.getPrecio());
            this.descripcion.setValue(value.getDescripcion());
            this.fechaInicio.setValue(value.getFechaInicio());
        }
    }
    @Override
	public void mostrarReservasEnGrid(List<Reservas> items) {
		Collection<Reservas> itemsCollection = items;
		grid.setItems(itemsCollection);
		this.elementos = items;
	}

	@Override
	public void mostrarMensajeError(String mensaje) {
		Notification.show(mensaje);
	}

	@Override
	public void mostrarMensajeExito(String mensaje) {
		Notification.show(mensaje);
	}
}