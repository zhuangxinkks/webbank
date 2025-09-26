const API_BASE = 'webbank';

// 页面加载时检查登录状态
document.addEventListener('DOMContentLoaded', function() {
    checkLoginStatus();
});

async function checkLoginStatus() {
    try {
        const response = await fetch(`${API_BASE}/checkLogin`);
        const result = await response.json();

        if (result.loggedIn) {
            showMainSection(result.userNumber);
        } else {
            showLoginSection();
        }
    } catch (error) {
        console.error('检查登录状态失败:', error);
    }
}

function showLoginSection() {
    document.getElementById('loginSection').style.display = 'block';
    document.getElementById('registerSection').style.display = 'none';
    document.getElementById('mainSection').style.display = 'none';
    document.getElementById('userInfo').style.display = 'none';
}

function showRegister() {
    document.getElementById('loginSection').style.display = 'none';
    document.getElementById('registerSection').style.display = 'block';
}

function showMainSection(userNumber) {
    document.getElementById('loginSection').style.display = 'none';
    document.getElementById('registerSection').style.display = 'none';
    document.getElementById('mainSection').style.display = 'block';
    document.getElementById('userInfo').style.display = 'flex';

    document.getElementById('welcomeMsg').textContent = `欢迎, ${userNumber}`;
    loadAccounts();
}

// 登录表单提交
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const userNumber = document.getElementById('loginUserNumber').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const response = await fetch(`${API_BASE}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `userNumber=${encodeURIComponent(userNumber)}&userPassword=${encodeURIComponent(password)}`
        });

        const result = await response.json();

        if (result.success) {
            showMainSection(userNumber);
            showMessage('登录成功', 'success');
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        showMessage('登录失败: ' + error.message, 'error');
    }
});

// 注册表单提交
document.getElementById('registerForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const userNumber = document.getElementById('regUserNumber').value;
    const password = document.getElementById('regPassword').value;

    try {
        const response = await fetch(`${API_BASE}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `userNumber=${encodeURIComponent(userNumber)}&userPassword=${encodeURIComponent(password)}`
        });

        const result = await response.json();

        if (result.success) {
            showMessage('注册成功，请登录', 'success');
            showLogin();
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        showMessage('注册失败: ' + error.message, 'error');
    }
});

function showLogin() {
    document.getElementById('loginSection').style.display = 'block';
    document.getElementById('registerSection').style.display = 'none';
}

async function logout() {
    try {
        const response = await fetch(`${API_BASE}/logout`);
        const result = await response.json();

        if (result.success) {
            showLoginSection();
            showMessage('注销成功', 'success');
        }
    } catch (error) {
        showMessage('注销失败: ' + error.message, 'error');
    }
}

function showSection(sectionId) {
    // 隐藏所有部分
    document.querySelectorAll('.section').forEach(section => {
        section.style.display = 'none';
    });

    // 显示选中的部分
    document.getElementById(sectionId).style.display = 'block';
}

async function loadAccounts() {
    try {
        const response = await fetch(`${API_BASE}/getAllAccounts`);
        const result = await response.json();

        const accountsList = document.getElementById('accountsList');

        if (result.success) {
            if (result.allAccounts && result.allAccounts.length > 0) {
                accountsList.innerHTML = result.allAccounts.map(account => `
                    <div class="account-item">
                        <p>账户号码: ${account.accountNumber}</p>
                        <p>余额: ¥${account.accountBalance}</p>
                        <p>所属用户: ${account.userNumber}</p>
                    </div>
                `).join('');
            } else {
                accountsList.innerHTML = '<p>暂无账户</p>';
            }
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        showMessage('加载账户失败: ' + error.message, 'error');
    }
}

async function createAccount() {
    const accountNumber = prompt('请输入新账户号码:');

    if (accountNumber) {
        try {
            const response = await fetch(`${API_BASE}/createAccount`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `accountNumber=${encodeURIComponent(accountNumber)}`
            });

            const result = await response.json();

            if (result.success) {
                showMessage('账户创建成功', 'success');
                loadAccounts();
            } else {
                showMessage(result.message, 'error');
            }
        } catch (error) {
            showMessage('创建账户失败: ' + error.message, 'error');
        }
    }
}

async function deposit() {
    const accountNumber = document.getElementById('depositAccount').value;
    const amount = parseFloat(document.getElementById('depositAmount').value);

    if (!accountNumber || isNaN(amount) || amount <= 0) {
        showMessage('请输入有效的账户号码和金额', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/deposit`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `accountNumber=${encodeURIComponent(accountNumber)}&amount=${amount}`
        });

        const result = await response.json();

        if (result.success) {
            showMessage(`存款成功，新余额: ¥${result.newAccountBalance}`, 'success');
            loadAccounts();
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        showMessage('存款失败: ' + error.message, 'error');
    }
}

async function withdraw() {
    const accountNumber = document.getElementById('withdrawAccount').value;
    const amount = parseFloat(document.getElementById('withdrawAmount').value);

    if (!accountNumber || isNaN(amount) || amount <= 0) {
        showMessage('请输入有效的账户号码和金额', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/withdraw`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `accountNumber=${encodeURIComponent(accountNumber)}&amount=${amount}`
        });

        const result = await response.json();

        if (result.success) {
            showMessage(`取款成功，新余额: ¥${result.newAccountBalance}`, 'success');
            loadAccounts();
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        showMessage('取款失败: ' + error.message, 'error');
    }
}

async function changePassword() {
    const oldPassword = document.getElementById('oldPassword').value;
    const newPassword = document.getElementById('newPassword').value;

    if (!oldPassword || !newPassword) {
        showMessage('请输入旧密码和新密码', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/changePassword`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `oldUserPassword=${encodeURIComponent(oldPassword)}&newUserPassword=${encodeURIComponent(newPassword)}`
        });

        const result = await response.json();

        if (result.success) {
            showMessage('密码修改成功', 'success');
            document.getElementById('oldPassword').value = '';
            document.getElementById('newPassword').value = '';
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        showMessage('修改密码失败: ' + error.message, 'error');
    }
}

function showMessage(message, type) {
    // 移除现有的消息
    const existingMessage = document.querySelector('.message');
    if (existingMessage) {
        existingMessage.remove();
    }

    // 创建新消息
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;

    // 插入到容器顶部
    const container = document.querySelector('.container');
    container.insertBefore(messageDiv, container.firstChild);

    // 3秒后自动消失
    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}