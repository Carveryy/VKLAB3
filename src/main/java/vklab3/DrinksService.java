package vklab3;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Named("drinkService")
@ViewScoped
public class DrinksService implements Serializable {

    @PersistenceContext(unitName = "VKLAB3_PU")
    private EntityManager em;
    private List<Drink> drinks;
    private Drink currentDrink;

    @PostConstruct
    public void init() {
        loadDrinks();
        currentDrink = new Drink();
    }

    public void loadDrinks() {
        drinks = em.createQuery("SELECT d FROM Drink d", Drink.class).getResultList();
    }

    @Transactional
    public void saveDrink() {
        if (currentDrink.getId() == null) {
            em.persist(currentDrink);
        } else {
            em.merge(currentDrink);
        }
        loadDrinks();
        currentDrink = new Drink(); // Очищуємо форму
    }

    @Transactional
    public void deleteDrink(Drink drink) {
        Drink toRemove = em.merge(drink);
        em.remove(toRemove);
        loadDrinks();
    }

    public void editDrink(Drink drink) {
        this.currentDrink = drink;
    }

    public void prepareNewDrink() {
        this.currentDrink = new Drink();
    }

    public List<Drink> getDrinks() { return drinks; }
    public Drink getCurrentDrink() { return currentDrink; }
    public void setCurrentDrink(Drink currentDrink) { this.currentDrink = currentDrink; }
}