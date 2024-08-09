import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../redux/hooks';
import { fetchAccountsForCustomer, resetAccountStatus } from '../features/accountSlice';
import { Form, Button, Table } from 'react-bootstrap';

const AccountsPage = () => {
    const [selectedCustomer, setSelectedCustomer] = useState('');
    const [selectedCurrency, setSelectedCurrency] = useState('');
    const dispatch = useAppDispatch();
    
    const { customers } = useAppSelector((state) => state.customer);
    const { accounts, status, error } = useAppSelector((state) => state.account);
    console.log('CUSTOMERS= ' + customers);

    useEffect(() => {
        
        dispatch(fetchAccountsForCustomer()); // Müşterileri yükle
    }, [dispatch]); 

    const handleSearch = () => {
        if (selectedCustomer) {
            dispatch(fetchAccountsForCustomer({ customerId: selectedCustomer, currencyType: selectedCurrency }));
        }
    };

    const handleReset = () => {
        setSelectedCustomer('');
        setSelectedCurrency('');
        dispatch(resetAccountStatus()); // Redux state sıfırlama
    };

    return (
        <div className="container mt-5">
            <h3>Müşteri Hesap Sorgulama</h3>
            <Form className="mb-4">
                <Form.Group controlId="customerSelect">
                    <Form.Label>Müşteri Seçin</Form.Label>
                    <Form.Control
                        as="select"
                        value={selectedCustomer}
                        onChange={(e) => setSelectedCustomer(e.target.value)}
                    >
                        <option value="">Müşteri Seçin</option>
                        {customers.map((customer) => (
                            <option key={customer.id} value={customer.id}>
                                {customer.name}
                            </option>
                        ))}
                    </Form.Control>
                </Form.Group>

                {selectedCustomer && (
                    <Form.Group controlId="currencySelect">
                        <Form.Label>Döviz Türü Seçin</Form.Label>
                        <Form.Control
                            as="select"
                            value={selectedCurrency}
                            onChange={(e) => setSelectedCurrency(e.target.value)}
                        >
                            <option value="">Tüm Döviz Türleri</option>
                            {accounts
                                .map((account) => account.currencyType)
                                .filter((value, index, self) => self.indexOf(value) === index)
                                .map((currencyType) => (
                                    <option key={currencyType} value={currencyType}>
                                        {currencyType}
                                    </option>
                                ))}
                        </Form.Control>
                    </Form.Group>
                )}

                <Button variant="primary" onClick={handleSearch} className="mr-2">
                    Sorgula
                </Button>
                <Button variant="secondary" onClick={handleReset}>
                    Temizle
                </Button>
            </Form>

            {status === 'loading' && <p>Yükleniyor...</p>}
            {error && <p>Hata: {error}</p>}

            {accounts.length > 0 && (
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>İsim Soyisim</th>
                            <th>Bakiye</th>
                            <th>Döviz Tipi</th>
                            <th>TL Değeri</th>
                        </tr>
                    </thead>
                    <tbody>
                        {accounts.map((account) => (
                            <tr key={account.id}>
                                <td>{customers.find((c) => c.id === parseInt(selectedCustomer)).name}</td>
                                <td>{account.balance}</td>
                                <td>{account.currencyType}</td>
                                <td>
                                    {account.currencyType !== 'TL'
                                        ? (account.balance * getCurrencyRate(account.currencyType)).toFixed(2)
                                        : '-'}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </div>
    );
};


export default AccountsPage;
