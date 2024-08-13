
const EndOfDayPage = () => {
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
                    <button className="btn btn-primary w-100">Gün Sonu İşlemleri Başlat</button>
                  </div>
                  <div className="col-md-4">
                    <button className="btn btn-danger w-100">Günü Kapat</button>
                  </div>
                  <div className="col-md-4">
                  <button className="btn btn-primary w-100">Günü Başlat</button>
                  </div>
                </div>
  
                <div className="row">
                  <div className="col-md-12">
                    <h5>Gün Sonu Raporları</h5>
                    <ul className="list-group">
                      <li className="list-group-item">01.01.2024Müşteri_Tabloları.xlsx</li>
                      <li className="list-group-item">01.01.2024_Hesap_Tabloları.xlsx</li>
                      <li className="list-group-item">01.01.2024İşlem_Tabloları.xlsx</li>
                      <li className="list-group-item">01.01.2024_Nakit_Akış_Tabloları.xlsx</li>
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