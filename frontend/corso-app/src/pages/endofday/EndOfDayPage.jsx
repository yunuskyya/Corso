import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAppDispatch } from '../../redux/hooks';
import { fetchSystemDate } from '../../features/systemDateSlice';
import {
  END_OF_DAY_CLOSE_DAY,
  END_OF_DAY_IS_DAY_CLOSED,
  END_OF_DAY_START_CLOSE_DAY,
  END_OF_DAY_IS_DAY_CLOSE_STARTED,
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
  const dispatch = useAppDispatch();
  const [reports, setReports] = useState([]);
  const [isDayCloseStarted, setIsDayCloseStarted] = useState(false);
  const [isDayClosed, setIsDayClosed] = useState(false);

  useEffect(() => {
    const checkDayStatus = async () => {
      try {
        const [closeStartedResponse] = await Promise.all([
          axios.get(END_OF_DAY_IS_DAY_CLOSE_STARTED)
        ]);
        setIsDayCloseStarted(closeStartedResponse.data);
      } catch (error) {
        console.error('Error checking day status:', error);
      }
    };

    checkDayStatus();
  }, []);

  const handleStartEndOfDay = async () => {
    try {
      await axios.get(END_OF_DAY_START_CLOSE_DAY);
      fetchReports(); // Raportları al
      // Gün sonu işlemlerini başlatma tamamlandığında buton durumlarını güncelle
      const { data } = await axios.get(END_OF_DAY_IS_DAY_CLOSE_STARTED);
      setIsDayCloseStarted(data);
      dispatch(fetchSystemDate()); // Sistem tarihini güncelle
    } catch (error) {
      console.error('Error starting end of day process:', error);
    }
  };

  const handleCloseDay = async (date) => {
    if (window.confirm(`Yeni sistem tarihi: ${date}. Onaylıyor musunuz?`)) {
      try {
        // Tarihi String olarak gönderiyoruz
        await axios.post(END_OF_DAY_CLOSE_DAY, { date }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        const { data } = await axios.get(END_OF_DAY_IS_DAY_CLOSED);
        setIsDayClosed(data);
        setIsDayCloseStarted(false); // Günü kapatma tamamlandığında buton durumlarını güncelle
        dispatch(fetchSystemDate()); // Sistem tarihini güncelle
        window.location.reload();
      } catch (error) {
        console.error('Error closing day:', error);
      }
    }
  };

  const fetchReports = async () => {
    try {
      const reports = [
        { name: 'Gün Sonu Nakit Akış (Excel)', url: REPORTS_MONEY_TRANSFER_FETCH_ALL_EXCEL, type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' },
        { name: 'Gün Sonu Nakit Akış (PDF)', url: REPORTS_MONEY_TRANSFER_FETCH_ALL_PDF, type: 'application/pdf' },
        { name: 'Gün Sonu Müşteri (Excel)', url: REPORTS_CUSTOMERS_FETCH_ALL_EXCEL, type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' },
        { name: 'Gün Sonu Müşteri (PDF)', url: REPORTS_CUSTOMERS_TRANSFER_FETCH_ALL_PDF, type: 'application/pdf' },
        { name: 'Gün Sonu Hesap (Excel)', url: REPORTS_ACCOUNTS_FETCH_ALL_EXCEL, type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' },
        { name: 'Gün Sonu Hesap (PDF)', url: REPORTS_ACCOUNTS_TRANSFER_FETCH_ALL_PDF, type: 'application/pdf' },
        { name: 'Gün Sonu İşlem (Excel)', url: REPORTS_TRANSACTIONS_FETCH_ALL_EXCEL, type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' },
        { name: 'Gün Sonu İşlem (PDF)', url: REPORTS_TRANSACTIONS_TRANSFER_FETCH_ALL_PDF, type: 'application/pdf' }
      ];

      // Dosyaları fetch et ve uygun şekilde ayarla
      const fetchedReports = await Promise.all(
        reports.map(async (report) => {
          const response = await axios.get(report.url, { responseType: 'blob' });
          return {
            ...report,
            url: URL.createObjectURL(new Blob([response.data], { type: report.type }))
          };
        })
      );

      setReports(fetchedReports);
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
                  <button
                    className="btn btn-primary w-100"
                    onClick={handleStartEndOfDay}
                    disabled={isDayCloseStarted}
                  >
                    Gün Sonu İşlemleri Başlat
                  </button>
                </div>
                <div className="col-md-4">
                  <button
                    className="btn btn-danger w-100"
                    onClick={() => {
                      const date = document.getElementById('date').value;
                      if (date) {
                        handleCloseDay(date);
                      } else {
                        alert('Lütfen tarih seçiniz.');
                      }
                    }}
                    disabled={!isDayCloseStarted}
                  >
                    Günü Kapat
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
