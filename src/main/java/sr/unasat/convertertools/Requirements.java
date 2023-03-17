package sr.unasat.convertertools;

/*
Bouw een "Converter" applicatie met een GUI. De applicatie taal is Java en wordt geen gebruik gemaakt van een database.
 
De applicatie bevat:
 - een morse code omzetter
 - minimaal een andere omzetter (naar keuze)
 - een met Swing of JavaFX gemaakte interface 
 - tenminste 2 velden (1 input en 1 output)
 - TODO: de namen en studentencodes van alle groepsleden
 - per groepslid een korte samenvatting van het voor hun makkelijkste en moeilijkste deel van het project en hoe ze daarmee zijn omgegaan (min: 100 woorden, mas: 200 woorden)

Andere eisen:
 - maximaal 5 studenten per groep
 - unit testing
 - exception handeling
 - getters en setters
 - gebruik deze interface

Bonus punten:
 - De morse code output door middel van geluid
 
Inleveren:
 - Het werkende Java project. LET OP: Dit betekent de volledig project directory die importeerbaar is en niet alleen Java files.
 - Overzicht groepsleden en studentnr
 - demo film
 - presentatie
 - elk groepslid moet inleveren via email met als subject "JAVA 2 BP 2023"
 - uiterlijke inleverdatum: 19 maart 2023
 */

public interface Requirements {

    /*
     * List of members in the group
     */
    String[] groepsleden();

    /*
     * Convert char to morse code
     */
    String abs2morse(char inputChar);

    /*
     * Convert morse code to char
     */
    char morse2abc(String inputString);

    /*
     * Convert from input field
     */
    void convert();

    /*
     * swap input and output fields data
     */
    void swap();

    /*
     * Clear inout and output data
     */
    void clear();

    /*
     * Example input for morse code to abc
     */
    String exampleMorseCode();

    /*
     * Example input for morse code to abc
     */
    String exampleString();

    /*
     * Help
     */
    String explain();
}