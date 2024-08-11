
const AddAccountPage = () => {
    const [account, setAccount] = useState({
        name: '',
        email: '',
        password: '',
    });

    const handleChange = (e) => {
        setAccount({
            ...account,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(account);
    };

    return (
        <div>
            <h1>Add Account</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="name"
                    value={account.name}
                    onChange={handleChange}
                    placeholder="Name"
                />
                <input
                    type="email"
                    name="email"
                    value={account.email}
                    onChange={handleChange}
                    placeholder="Email"
                />
                <input
                    type="password"
                    name="password"
                    value={account.password}
                    onChange={handleChange}
                    placeholder="Password"
                />
                <button type="submit">Add Account</button>
            </form>
        </div>
    );
};

export default AddAccountPage;