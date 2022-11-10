import { Loan } from "./Loan"
import { LoanPage } from "./LoanPage"

export const LOANS_DATA: LoanPage = {
    content: [
        {id: 1, 
            game: {id: 1, title: "Juego 1", age: 14, category: { id: 3, name: 'Categoría 3' }, 
            author: { id: 1, name: 'Autor 1', nationality: 'Nacionalidad 1'}}, 
            client: {id: 1, name: "Cliente 1"},
            loanDate: new Date('10-2-2022'),
            returnDate: new Date('10-12-2022')
        },

        {id: 2, 
            game: {id: 2, title: "Juego 2", age: 8, category: { id: 1, name: 'Categoría 1' }, 
            author: { id: 1, name: 'Autor 1', nationality: 'Nacionalidad 1'}}, 
            client: {id: 2, name: "Cliente 2"},
            loanDate: new Date('10-2-2022'),
            returnDate: new Date('10-12-2022')
        },

        {id: 3, 
            game: {id: 1, title: "Juego 1", age: 14, category: { id: 3, name: 'Categoría 3' }, 
            author: { id: 3, name: 'Autor 3', nationality: 'Nacionalidad 3'}}, 
            client: {id: 2, name: "Cliente 2"},
            loanDate: new Date('10-12-2022'),
            returnDate: new Date('10-22-2022')
        },

        {id: 4, 
            game: {id: 4, title: "Juego 4", age: 14, category: { id: 1, name: 'Categoría 1' }, 
            author: { id: 3, name: 'Autor 3', nationality: 'Nacionalidad 3'}}, 
            client: {id: 5, name: "Cliente 5"},
            loanDate: new Date('10-12-2022'),
            returnDate: new Date('10-22-2022')
        },

        {id: 5, 
            game: {id: 2, title: "Juego 2", age: 8, category: { id: 3, name: 'Categoría 3' }, 
            author: { id: 3, name: 'Autor 3', nationality: 'Nacionalidad 3'}}, 
            client: {id: 2, name: "Cliente 2"},
            loanDate: new Date('10-2-2022'),
            returnDate: new Date('10-12-2022')
        },

        {id: 6, 
            game: {id: 1, title: "Juego 1", age: 14, category: { id: 3, name: 'Categoría 3' }, 
            author: { id: 3, name: 'Autor 3', nationality: 'Nacionalidad 3'}}, 
            client: {id: 2, name: "Cliente 2"},
            loanDate: new Date('10-23-2022'),
            returnDate: new Date('10-25-2022')
        }
    ],
    pageable : {
        pageSize: 5,
        pageNumber: 0,
        sort: [
            {property: "id", direction: "ASC"} 
        ]
    },
    totalElements: 6
}

