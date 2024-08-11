import React from 'react';

const IbanPage = () => {
  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header text-center" style={{ backgroundColor: '#800000', color: 'white' }}>
              <h3>IBAN İşlemleri</h3>
            </div>
            <div className="card-body">
              <form>
                <div className="mb-3">
                  <label htmlFor="customer" className="form-label">Müşteri</label>
                  <input type="text" className="form-control" id="customer" placeholder="Müşteri girin" />
                </div>
                <div className="mb-3">
                  <label htmlFor="addIban" className="form-label">Eklenecek IBAN</label>
                  <input type="text" className="form-control" id="addIban" placeholder="Eklenecek IBAN girin" />
                </div>
                <div className="mb-3">
                  <label htmlFor="nickname" className="form-label">Rumuz</label>
                  <input type="text" className="form-control" id="nickname" placeholder="Rumuz girin" />
                </div>
                <div className="mb-3">
                  <label htmlFor="removeIban" className="form-label">Çıkarılcak IBAN</label>
                  <input type="text" className="form-control" id="removeIban" placeholder="Çıkarılcak IBAN girin" />
                </div>
                <div className="row">
                  <div className="col-md-6">
                    <button type="submit" className="btn btn-primary w-100">
                      İşlemi Tamamla
                    </button>
                  </div>
                  <div className="col-md-6">
                    <button type="reset" className="btn btn-secondary w-100">
                      Temizle
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default IbanPage;
