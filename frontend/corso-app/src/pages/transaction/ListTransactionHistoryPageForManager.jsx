import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchTransactionListForManagerThunk } from './../../features/transactionSlice';
import useAuth from '../../hooks/useAuth';
import 'bootstrap/dist/css/bootstrap.min.css';  // Bootstrap 5.3

const ListTransactionHistoryPageForManager = () => {
    // const [startDate, setStartDate] = useState('');
    // const [endDate, setEndDate] = useState('');
    const [transactionList, setTransactionList] = useState([]);
    const dispatch = useAppDispatch();
    const { user } = useAuth();
    const userId = user?.id;


    const handleListClick = async () => {
            try {
                const transactions = await dispatch(fetchTransactionListForManagerThunk()).unwrap();
                setTransactionList(transactions);
            } catch (error) {
                console.error('Error fetching transactions:', error);
            }
    };

    const formatDate = (dateArray) => {
        if (!Array.isArray(dateArray) || dateArray.length !== 3) return '';
        const [year, month, day] = dateArray;
        return `${day.toString().padStart(2, '0')}.${month.toString().padStart(2, '0')}.${year}`;
    };


    const handleClearClick = () => {
        // setStartDate('');
        // setEndDate('');
        setTransactionList([]);
    };

    return (
        <div className="container mt-4">
            <div className="row mb-3">
                <div className="col-md-4">
                    <label className="form-label">
                        Başlangıç Tarihi:
                        <input
                            type="date"
                            className="form-control"
                        // value={startDate}
                        // onChange={(e) => setStartDate(e.target.value)}
                        />
                    </label>
                </div>
                <div className="col-md-4">
                    <label className="form-label">
                        Bitiş Tarihi:
                        <input
                            type="date"
                            className="form-control"
                        // value={endDate}
                        // onChange={(e) => setEndDate(e.target.value)}
                        />
                    </label>
                </div>
                <div className="col-md-4 d-flex align-items-end">
                    <button onClick={handleListClick} className="btn btn-primary me-2">
                        Listele
                    </button>
                    <button onClick={handleClearClick} className="btn btn-secondary">
                        Temizle
                    </button>
                </div>
            </div>
            <div>
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">Müşteri</th>
                            <th scope="col">Tarih</th>
                            <th scope="col">Döviz Kuru</th>
                            <th scope="col">Satın Alınan Döviz</th>
                            <th scope="col">Satılan Döviz</th>
                            <th scope="col">Maliyet</th>
                            <th scope="col">Alınan Döviz Miktarı</th>
                        </tr>
                    </thead>
                    <tbody>
                        {transactionList.length > 0 ? (
                            transactionList.map((transaction, index) => (
                                <tr key={index}>
                                    <td>{`${transaction.name} ${transaction.surname}`}</td>
                                    <td>{formatDate(transaction.transactionSystemDate)}</td>
                                    <td>{transaction.rate}</td>
                                    <td>{transaction.purchasedCurrency}</td>
                                    <td>{transaction.soldCurrency}</td>
                                    <td>{transaction.cost}</td>
                                    <td>{transaction.amount}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="4">Henüz işlem bulunmuyor.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

const styles = {
    container: {
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      marginTop: '2rem'
    },
    formGroup: {
      marginBottom: '1rem',
      width: '100%',
      maxWidth: '400px'
    },
    label: {
      display: 'block',
      marginBottom: '0.5rem'
    },
    select: {
      width: '100%'
    },
    input: {
      width: '100%'
    }
  };

export default ListTransactionHistoryPageForManager;
