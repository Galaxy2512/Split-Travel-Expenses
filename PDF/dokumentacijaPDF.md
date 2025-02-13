
# Java Swing aplikacija s MVC strukturom, koristeći Observer i Strategy Pattern

## Opis
Ova aplikacija simulira TriCount aplikaciju. Glavni cilj je stvoriti jednostavnu aplikaciju koja omogućuje dijeljenje računa među prijateljima. Aplikacija omogućuje korisnicima dodavanje prijatelja, dodavanje troškova i dijeljenje računa među prijateljima.

## Tehnologije
- Java
- Swing
- Baza podataka
- MVC (Model-View-Controller)
- Observer Pattern
- Strategy Pattern
- Maven

## Struktura projekta

### Model
Paket `Model` sadrži klase koje predstavljaju podatke i poslovnu logiku aplikacije.

#### Klase
- **Expense**: Predstavlja opći trošak.
- **ExpencesModel**: Upravljanje popisom troškova i obavještavanje promatrača o promjenama.
- **ExpencesObservable**: Sučelje za promatranje troškova.
- **ExpencesObserver**: Sučelje za promatrače troškova.
- **DatabaseConnection**: Upravljanje vezom s bazom podataka i operacijama nad podacima.
- **User**: Predstavlja korisnika s ID-om, imenom i stanjem računa.
- **UserModel**: Upravljanje popisom korisnika.

### View
Paket `View` sadrži klase koje predstavljaju korisničko sučelje aplikacije.

#### Klase
- **MenuBar**: Predstavlja traku izbornika s opcijama za izvoz, uvoz i brisanje podataka.
- **LeftPanel**: Predstavlja lijevu stranu panela gdje korisnici mogu unositi podatke o troškovima.
- **RightPanel**: Predstavlja desnu stranu panela gdje se prikazuju troškovi.
- **MenuBarEvent**: Predstavlja događaje pokrenute trakom izbornika.
- **MenuBarListener**: Sučelje za slušanje događaja trake izbornika.
- **MainFrame**: Predstavlja glavni prozor aplikacije.
- **ViewPanel**: Predstavlja glavni panel koji sadrži lijevi i desni panel.
- **RightPanelEvent**: Predstavlja događaj koji se događa u `RightPanel`.
- **RightPanelListener**: Sučelje za slušanje događaja na desnoj strani panela.
- **UserSummaryPanel**: Predstavlja panel koji prikazuje sažetak troškova korisnika.

### Controller
Paket `Controller` sadrži klase koje upravljaju interakcijom između modela i pogleda.

#### Klase
- **DataBaseController**: Glavna klasa aplikacije.
- **Controller**: Upravljanje komunikacijom između modela i pogleda, obrada događaja i rukovanje uvozom/izvozom podataka.
- **DataHandle**: Upravljanje operacijama uvoza i izvoza podataka.
- **LeftPanelController**: Upravljanje interakcijama lijevog panela.
- **RightPanelController**: Upravljanje interakcijama desnog panela.

## Korišteni obrasci

### MVC (Model-View-Controller)
- **Model**: Upravljanje podacima i poslovnom logikom.
- **View**: Prikaz podataka i slanje korisničkih akcija kontroleru.
- **Controller**: Obrada korisničkog unosa, ažuriranje modela i osvježavanje pogleda.

### Observer Pattern
- **ExpencesObservable**: Sučelje za dodavanje i obavještavanje promatrača.
- **ExpencesObserver**: Sučelje za ažuriranje promatrača kada se model promijeni.

### Strategy Pattern
- **Expense**: Osnovna klasa za različite vrste troškova.


## Slika aplikacije

![Aplikacija](aplikacija.png)


## Slika dodavanja korisnika

![Aplikacija](user.png)



## Slika sučelja

![Aplikacija](aplikacija_sucelje.png)


## Slika izvoza podataka

![Aplikacija](aplikacija_load_data.png)

![Aplikacija](data_added_from_DB.png)


#

## Korištenje
1. **Dodavanje korisnika**: Koristite lijevi panel za dodavanje prijatelja.
2. **Dodavanje troškova**: Unesite podatke o troškovima u lijevi panel i pošaljite.
3. **Dijeljenje računa**: Desni panel prikazuje podijeljene troškove.
4. **Izvoz podataka**: Koristite traku izbornika za izvoz podataka u CSV, tekstualni ili binarni format.
5. **Uvoz podataka**: Koristite traku izbornika za uvoz podataka iz datoteke.
6. **Brisanje svih podataka iz baze podataka**: Koristite traku izbornika za brisanje svih podataka.
7. **Prikaz podataka**: Prikazuje podatke o korisnicima, troškovima i dijeljenju računa iz baze podataka te se mogu nadodavati novi podaci.

## Dijagram klasa

![Dijagram klasa](dijagram.png)


#### Controller
Centralna klasa koja upravlja interakcijama između pogleda (View) i modela (Model). Obično obrađuje događaje i osigurava da odgovarajući podaci teku između GUI komponenata i logike aplikacije.

#### MainFrame
Glavni okvir aplikacije, vjerojatno predstavlja GUI prozor koji sadrži sve ostale panele i elemente. Ovdje bi trebali biti postavljeni ključni dijelovi aplikacije, kao što su meni, lijevi i desni panel.

#### UserModel
Model koji upravlja podacima korisnika. Služi za spremanje i dohvaćanje informacija o korisnicima (npr. ime, saldo itd.).

### Paneli

#### LeftPanel i RightPanel
Prikazuju različite funkcionalnosti u GUI-ju. LeftPanel sadrži unos podataka (korisnika, troškova itd.). RightPanel prikazuje informacije kao što su salda korisnika ili podaci o troškovima.

#### ViewPanel
Kombinira lijevi i desni panel, vjerojatno koristi `JSplitPane` za horizontalnu podjelu.

#### UserSummaryPanel
Vjerojatno prikazuje sažetke o korisnicima, poput ukupnih troškova ili dugovanja.

### Eventi i Listeneri

#### LeftPanelEvent i RightPanelEvent
Predstavljaju događaje koji se šalju iz lijevog ili desnog panela, npr. kad korisnik unese podatke ili izvrši neku akciju.

#### LeftPanelListener i RightPanelListener
Interfejsi ili klase koje reagiraju na događaje panela. Implementiraju logiku koja se pokreće kada određeni panel prijavi promjenu ili akciju.

#### MenuBarEvent i MenuBarListener
Odnose se na događaje u meniju (npr. izvoz podataka, uvoz balansa).

### Observable i Observer

#### ExpensesObservable i ExpensesObserver
Implementacija obrasca Observer. Observable omogućuje panelima ili komponentama da se pretplate na promjene u podacima (npr. troškovi). Observer prima obavijesti kada dođe do promjena.

### Model i Podaci

#### ExpensesModel
Klasa koja upravlja troškovima i sadrži podatke o troškovima putovanja.

#### Expense
Predstavlja pojedinačni trošak (npr. kategorija troška, iznos, korisnik koji ga je platio).

#### ExpenseCategory
Kategorije troškova, kao što su hrana, smještaj, prijevoz itd.

#### DatabaseConnection
Klasa za rad s bazom podataka. Upravlja spremanjem i dohvaćanjem podataka.

#### DataHandle
Pomoćna klasa za obradu podataka, kao što je validacija ili manipulacija podacima prije spremanja.

### Kontroleri

#### LeftPanelController i RightPanelController
Specifični kontroleri za lijevi i desni panel. Osiguravaju logiku specifičnu za funkcionalnosti tih panela.

### Aplikacija

#### App
Ulazna točka aplikacije koja inicijalizira sve komponente (GUI, kontrolere, modele itd.).

## ER dijagram

![ER dijagram](img.png)

# Struktura Baze Podataka za Praćenje Troškova

Ova baza podataka je dizajnirana za upravljanje troškovima i dugovanjima između korisnika. Sastoji se od četiri glavne tabele: korisnici, troškovi, poveznica između korisnika i troškova, te dugovanja.

## Tabele

### 1. **Tabela `users` (korisnici)**

Tabela `users` čuva informacije o korisnicima koji sudjeluju u podjeli troškova.

| Kolona   | Tip          | Opis                                      |
|----------|--------------|-------------------------------------------|
| `id`     | INT          | Jedinstveni identifikator korisnika (primarni ključ, auto-increment). |
| `name`   | VARCHAR(255) | Ime korisnika (jedinstveno, obavezno).    |

#### Relacije
- Ova tabela je referenca za određivanje tko je platio trošak (`paid_by` u tabeli `expenses`) i tko sudjeluje u troškovima (`user_id` u tabeli `expense_users`).

---

### 2. **Tabela `expenses` (troškovi)**

Tabela `expenses` pohranjuje detalje o svakom pojedinačnom trošku.

| Kolona    | Tip          | Opis                                      |
|-----------|--------------|-------------------------------------------|
| `id`      | INT          | Jedinstveni identifikator troška (primarni ključ, auto-increment). |
| `name`    | VARCHAR(255) | Naziv troška (npr. "Večera", "Benzin").   |
| `category`| VARCHAR(50)  | Kategorija troška (npr. "Hrana", "Putovanje"). |
| `paid_by` | INT          | ID korisnika koji je platio trošak (stranjski ključ na tabelu `users`). |
| `date`    | DATE         | Datum troška.                            |
| `amount`  | DECIMAL(10,2)| Iznos troška.                            |

#### Relacije
- `paid_by` je stranjski ključ koji se referencira na `id` u tabeli `users`.
- Ako se korisnik izbriše, svi troškovi koje je platio taj korisnik automatski se brišu (`ON DELETE CASCADE`).

---

### 3. **Tabela `expense_users` (poveznica korisnika i troškova)**

Tabela `expense_users` modelira relaciju **Many-to-Many** između korisnika i troškova.

| Kolona       | Tip | Opis                                      |
|--------------|-----|-------------------------------------------|
| `expense_id` | INT | ID troška (stranjski ključ na tabelu `expenses`). |
| `user_id`    | INT | ID korisnika (stranjski ključ na tabelu `users`). |

#### Relacije
- Povezuje korisnike i troškove tako da jedan trošak može biti podijeljen između više korisnika.
- Ako se korisnik ili trošak izbrišu, odgovarajući redovi u ovoj tabeli automatski se brišu (`ON DELETE CASCADE`).

#### Primarni Ključ
- Kombinacija `expense_id` i `user_id` čini složeni primarni ključ, osiguravajući jedinstvenost unosa.

---

### 4. **Tabela `debts` (dugovanja)**

Tabela `debts` bilježi dugovanja između korisnika koja proizlaze iz podjele troškova.

| Kolona       | Tip          | Opis                                      |
|--------------|--------------|-------------------------------------------|
| `id`         | INT          | Jedinstveni identifikator dugovanja (primarni ključ, auto-increment). |
| `debtor_id`  | INT          | ID korisnika koji duguje novac (stranjski ključ na tabelu `users`). |
| `creditor_id`| INT          | ID korisnika kojemu se duguje novac (stranjski ključ na tabelu `users`). |
| `expense_id` | INT          | ID troška na kojem se temelji dugovanje (stranjski ključ na tabelu `expenses`). |
| `amount`     | DECIMAL(10,2)| Iznos dugovanja.                         |

#### Relacije
- `debtor_id` i `creditor_id` povezuju korisnike u odnosu "dužnik-kreditor".
- `expense_id` povezuje dugovanje s određenim troškom.
- Ako se korisnik ili trošak izbrišu, odgovarajuća dugovanja automatski se brišu (`ON DELETE CASCADE`).

---

## Shema Relacija Između Tabela

- **Tabela `users`**:
    - Jedan korisnik može biti povezan s više troškova (kao platiša ili sudionik).
    - Jedan korisnik može biti povezan s više dugovanja (kao dužnik ili kreditor).

- **Tabela `expenses`**:
    - Jedan trošak ima jednog platišu (`paid_by`).
    - Jedan trošak može biti povezan s više korisnika (kroz tabelu `expense_users`).

- **Tabela `expense_users`**:
    - Spaja korisnike i troškove u relaciju **Many-to-Many**.

- **Tabela `debts`**:
    - Povezuje dugovanja između korisnika na temelju određenog troška.

---

## Primjer Podataka

### Tabela `users`
| id  | name   |
|-----|--------|
| 1   | Kiki   |
| 2   | Lili   |

### Tabela `expenses`
| id  | name    | category | paid_by | date       | amount |
|-----|---------|----------|---------|------------|--------|
| 1   | Večera  | Hrana    | 1       | 2025-02-10 | 100.00 |

### Tabela `expense_users`
| expense_id | user_id |
|------------|---------|
| 1          | 1       |
| 1          | 2       |

### Tabela `debts`
| id  | debtor_id | creditor_id | expense_id | amount |
|-----|-----------|-------------|------------|--------|
| 1   | 2         | 1           | 1          | 50.00  |

---

## Zaključak

Ova struktura baze podataka omogućava praćenje troškova, korisnika i njihovih dugovanja u aplikaciji za upravljanje financijama ili podjelu troškova. Omogućava fleksibilnost i automatsko održavanje referencijalne integracije među tabelama.


## Zaključak
Ova aplikacija pruža jednostavan i učinkovit način za upravljanje i dijeljenje troškova među prijateljima koristeći dobro strukturiranu MVC arhitekturu i dizajnerske obrasce.