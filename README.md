 GlucideCalc - Prima mea aplicație Android

Acesta este primul meu proiect dezvoltat în Android Studio folosind Kotlin.

 Originea Proiectului
Proiectul a pornit ca un exercițiu de învățare bazat pe un tutorial de tip "Tip Calculator" 
(Calculator de Bacșiș), vazut pe YouTube. 
Deși structura matematică este similară, am ales să refactorizez și să adaptez întreaga logică 
pentru a servi unei nevoi mult mai practice: calculul rapid al carbohidraților pentru persoanele care 
trebuie să monitorizeze riguros nutriția (ex: diabetici sau sportivi).

 Ce am învățat realizând acest proiect:
    UI/UX Design: Am personalizat elementele vizuale pentru a se potrivi contextului nutrițional.
    Componente Android: Utilizarea EditText_pentru input, SeekBar_pentru selectarea procentelor și
    TextView_pentru afișarea rezultatelor în timp real.
    Logică în Kotlin: Implementarea unui sistem de calcul reactiv care se actualizează instantaneu
    la orice modificare a inputului.
    Siguranța datelor: Gestionarea erorilor de input (câmpuri goale sau caractere invalide) pentru 
     a preveni închiderea aplicației (crash).

 Cum funcționează:
    Utilizatorul introduce greutatea alimentului în grame.
    Selectează procentul de carbohidrați specific acelui aliment folosind slider-ul (SeekBar).
    Aplicația afișează instantaneu cantitatea netă de glucide ce urmează a fi consumată.


