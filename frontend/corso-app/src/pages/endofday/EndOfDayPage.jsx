import React, { useState } from 'react';
import axios from 'axios';
import {
  END_OF_DAY_START_CLOSE_DAY,
  REPORTS_MONEY_TRANSFER_FETCH_ALL_EXCEL,
  REPORTS_MONEY_TRANSFER_FETCH_ALL_PDF,
  REPORTS_CUSTOMERS_FETCH_ALL_EXCEL,
  REPORTS_CUSTOMERS_TRANSFER_FETCH_ALL_PDF,
  REPORTS_ACCOUNTS_FETCH_ALL_EXCEL,
  REPORTS_ACCOUNTS_TRANSFER_FETCH_ALL_PDF,
  REPORTS_TRANSACTIONS_FETCH_ALL_EXCEL,
  REPORTS_TRANSACTIONS_TRANSFER_FETCH_ALL_PDF
} from '../../constants/apiUrl';

const EndOfDayPage = () => {
  const [reports, setReports] = useState([]);

  const handleStartEndOfDay = async () => {
    try {
      const response = await axios.get(END_OF_DAY_START_CLOSE_DAY);
      // Eğer başarılıysa, raporları almak için API çağrısı yap
      fetchReports();
    } catch (error) {
      console.error('Error starting end of day process:', error);
    }
  };

  const handleCloseDay = async (date) => {
    // Kullanıcının tarih onayı
    if (window.confirm(`Yeni sistem tarihi: ${date}. Onaylıyor musunuz?`)) {
      try {
        await axios.post(END_OF_DAY_START_CLOSE_DAY, { date });
        // Tarih onaylandıktan sonra işlemleri başlat
        fetchReports();
      } catch (error) {
        console.error('Error closing day:', error);
      }
    }
  };

  const fetchReports = async () => {
    try {
      const [moneyTransferExcel, moneyTransferPdf, customersExcel, customersPdf, accountsExcel, accountsPdf, transactionsExcel, transactionsPdf] = await Promise.all([
        axios.get(REPORTS_MONEY_TRANSFER_FETCH_ALL_EXCEL, { responseType: 'blob' }),
        axios.get(REPORTS_MONEY_TRANSFER_FETCH_ALL_PDF, { responseType: 'blob' }),
        axios.get(REPORTS_CUSTOMERS_FETCH_ALL_EXCEL, { responseType: 'blob' }),
        axios.get(REPORTS_CUSTOMERS_TRANSFER_FETCH_ALL_PDF, { responseType: 'blob' }),
        axios.get(REPORTS_ACCOUNTS_FETCH_ALL_EXCEL, { responseType: 'blob' }),
        axios.get(REPORTS_ACCOUNTS_TRANSFER_FETCH_ALL_PDF, { responseType: 'blob' }),
        axios.get(REPORTS_TRANSACTIONS_FETCH_ALL_EXCEL, { responseType: 'blob' }),
        axios.get(REPORTS_TRANSACTIONS_TRANSFER_FETCH_ALL_PDF, { responseType: 'blob' })
      ]);

      // Raporları bir diziye ekle
      setReports([
        { name: 'Gün Sonu Nakit Akış (Excel)', url: URL.createObjectURL(moneyTransferExcel.data) },
        { name: 'Gün Sonu Nakit Akış (PDF)', url: URL.createObjectURL(moneyTransferPdf.data) },
        { name: 'Gün Sonu Müşteri (Excel)', url: URL.createObjectURL(customersExcel.data) },
        { name: 'Gün Sonu Müşteri (PDF)', url: URL.createObjectURL(customersPdf.data) },
        { name: 'Gün Sonu Hesap (Excel)', url: URL.createObjectURL(accountsExcel.data) },
        { name: 'Gün Sonu Hesap (PDF)', url: URL.createObjectURL(accountsPdf.data) },
        { name: 'Gün Sonu İşlem (Excel)', url: URL.createObjectURL(transactionsExcel.data) },
        { name: 'Gün Sonu İşlem (PDF)', url: URL.createObjectURL(transactionsPdf.data) }
      ]);
    } catch (error) {
      console.error('Error fetching reports:', error);
    }
  };

  return (
    <div className="container mt-5">
      <div className="row">
        <div className="col-md-12">
          <div className="card">
            <div className="card-header text-center bg-primary text-white">
              <h3>Gün Sonu</h3>
            </div>
            <div className="card-body">
              <div className="row mb-3">
                <div className="col-md-4">
                  <label htmlFor="date" className="form-label">Tarih</label>
                  <input type="date" className="form-control" id="date" />
                </div>
              </div>

              <div className="row mb-3 justify-content-center">
                <div className="col-md-4">
                  <button className="btn btn-primary w-100" onClick={handleStartEndOfDay}>
                    Gün Sonu İşlemleri Başlat
                  </button>
                </div>
                <div className="col-md-4">
                  <button
                    className="btn btn-danger w-100"
                    onClick={() => {
                      const date = document.getElementById('date').value;
                      handleCloseDay(date);
                    }}
                  >
                    Günü Kapat
                  </button>
                </div>
                <div className="col-md-4">
                  <button className="btn btn-primary w-100">
                    Günü Başlat
                  </button>
                </div>
              </div>

              <div className="row">
                <div className="col-md-12">
                  <h5>Gün Sonu Raporları</h5>
                  <ul className="list-group">
                    {reports.map((report, index) => (
                      <li key={index} className="list-group-item">
                        <a href={report.url} download>{report.name}</a>
                      </li>
                    ))}
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EndOfDayPage;
