import useAuth from '../../hooks/useAuth';
import { Link } from 'react-router-dom';
import { ADMIN_OPERATIONS, MANAGER_OPERATIONS } from '../../constants/routes';

const AccordionNavs = ({ variant }) => {

    const { user, role } = useAuth();

    console.log("role for navs: ", role);

    const operations = () => {
        switch (role) {
            case 'ROLE_ADMIN': return ADMIN_OPERATIONS;
            case 'ROLE_MANAGER': return MANAGER_OPERATIONS;
            case 'ROLE_BROKER': return;
        }
    }

    return (
        variant === 'sidebar' ? <AccordionSidebar /> : <AccordionNav />
    );
};


function AccordionSidebar({ operations }) {
    return (
        <li className={`accordion`} id="accordionExample">
            <div className="accordion-item">
                <h2 className="accordion-header">
                    <button className="accordion-button fw-bold bg-light" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                        Müşteri İşlemleri
                    </button>
                </h2>
                <div id="collapseOne" className="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                    <div className="accordion-body">
                        <Link className="nav-link fs-6" to="/dashboard/addCustomer">Ekle</Link>
                    </div>
                    <div className="accordion-body nav-item">
                        <Link className="nav-link fs-6" to="/dashboard/listCustomer">Listele</Link>
                    </div>
                    <div className="accordion-body nav-item">
                        <Link className="nav-link fs-6" to="/dashboard/updateCustomer">Güncelle</Link>
                    </div>
                </div>
            </div>
            <div className="accordion-item">
                <h2 className="accordion-header">
                    <button className="accordion-button fw-bold bg-light" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="true" aria-controls="collapseOne">
                        Hesap İşlemleri
                    </button>
                </h2>
                <div id="collapseTwo" className="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                    <div className="accordion-body">
                        <Link className="nav-link fs-6" to="/addCustomer">Hesap Ekle</Link>
                    </div>
                </div>
            </div>
            <Link className="nav-link bg-light fs-6" to="/addCustomer">Hesap Ekle</Link>
            <Link className="nav-link fs-6" to="/addCustomer">Hesap Ekle</Link>
        </li>
    );
}

function AccordionNav({ operations }) {
    return (
        <li className="nav-item dropdown">
            <a className="nav-link dropdown-toggle" href="#" id="themeDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Müşteri İşlemleri
            </a>
            <ul className="dropdown-menu" aria-labelledby="themeDropdown">

                <li>
                    <Link className="dropdown-item" to='/addCustomer'>Ekle</Link>
                </li>
                <li>
                    <Link className="dropdown-item" to='/listCustomer'>Listele</Link>
                </li>
                <li>
                    <Link className="dropdown-item" to='/updateCustomer'>Güncelle</Link>
                </li>
            </ul>
            <a className="nav-link dropdown-toggle" href="#" id="themeDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Hesap İşlemleri
            </a>
            <ul className="dropdown-menu" aria-labelledby="themeDropdown">

                <li>
                    <Link className="dropdown-item" to='/addCustomer'>Ekle</Link>
                </li>
                <li>
                    <Link className="dropdown-item" to='/listCustomer'>Listele</Link>
                </li>
                <li>
                    <Link className="dropdown-item" to='/updateCustomer'>Güncelle</Link>
                </li>
            </ul>
        </li>
    );
}


function AccordionItem({ title, title_link, operations, bs_parent }) {
    return (
        title_link ? (
            <Link
                className="nav-link"
                to={title_link}
                role="button"
            >
                {title}
            </Link>
        ) : (
            <div className="accordion-item">
                <h2 className="accordion-header">
                    <button
                        className="accordion-button fw-bold bg-light"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target={`#${title}`}
                        aria-expanded="true"
                        aria-controls={title}
                    >
                        {title}
                    </button>
                </h2>
                <div
                    id={title}
                    className="accordion-collapse collapse show"
                    data-bs-parent={bs_parent}
                >
                    {operations.map((operation, index) => (
                        <div key={index} className="accordion-body">
                            <Link
                                className="nav-link"
                                to={operation.link}
                                role="button"
                            >
                                {operation.title}
                            </Link>
                        </div>
                    ))}
                </div>
            </div>
        )
    );
}

export default AccordionNavs;