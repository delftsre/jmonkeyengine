/**
 * 
 */
package com.jme3.renderer;

/**
 * Printer for IDList.
 * @author Tr0k
 *
 */
public class IDListPlainTextPrinter implements IListPrinter {

	private IDList idList;
	
	public IDListPlainTextPrinter(IDList idList) {
		this.idList = idList;
	}
	
    /**
     * Prints the contents of the lists
     */
	@Override
	public void printList() {
        if (idList.getNewLen() > 0){
            System.out.print("New List: ");
            for (int i = 0; i < idList.getNewLen(); i++){
                if (i == idList.getNewLen() -1)
                    System.out.println(idList.getNewListElem(i));
                else
                    System.out.print(idList.getNewListElem(i)+", ");
            }
        }
        if (idList.getOldLen() > 0){
            System.out.print("Old List: ");
            for (int i = 0; i < idList.getOldLen(); i++){
                if (i == idList.getOldLen() -1)
                    System.out.println(idList.getOldListElem(i));
                else
                    System.out.print(idList.getOldListElem(i)+", ");
            }
        }
	}

}
