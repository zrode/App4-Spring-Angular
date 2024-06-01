export interface AccountDetails {
    accountId:            string;
    balance:              number;
    accountOperationDTOS: AccountOperations[];
    currentPage:          number;
    totalPages:           number;
    size:                 number;
}

export interface AccountOperations {
    id:            number;
    operationDate: Date;
    amount:        number;
    type:          string;
    description:   string;
}