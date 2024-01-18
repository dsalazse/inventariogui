package com.cipres.application.views;

import com.cipres.application.control.TranslatorService;
import com.cipres.application.views.about.AboutView;
import com.cipres.application.views.helloworld.HelloWorldView;
import com.cipres.application.views.tablas.DatoView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    private H2 viewTitle;
    private final TranslatorService translatorService;


    @Autowired
    public MainLayout(TranslatorService translatorService) {
        this.translatorService = translatorService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();        
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        String title=translatorService.getTranslatedMessage("Aplicacion", getLocale(), null);
        H1 appName = new H1(title);
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
                
        String opcion1=translatorService.getTranslatedMessage("opcion1", getLocale(), null); 
        String opcion2=translatorService.getTranslatedMessage("opcion2", getLocale(), null); 
        String datoCrud =translatorService.getTranslatedMessage("cruddato", getLocale(), null);
        String grupoCrud =translatorService.getTranslatedMessage("crudgrupo", getLocale(), null);
        
        nav.addItem(new SideNavItem(opcion1, HelloWorldView.class, LineAwesomeIcon.GLOBE_SOLID.create()));
        nav.addItem(new SideNavItem(opcion2, AboutView.class, LineAwesomeIcon.FILE.create()));
        nav.addItem(new SideNavItem(datoCrud, DatoView.class, LineAwesomeIcon.TABLETS_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
