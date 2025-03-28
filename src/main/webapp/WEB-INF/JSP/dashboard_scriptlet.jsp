<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Tableau de Bord (Scriptlet)</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <h2 class="mb-4">Tableau de Bord (Scriptlet)</h2>
        
        <!-- Notifications -->
        <%
            List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
            if (notifications != null && !notifications.isEmpty()) {
        %>
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <h4 class="alert-heading">Notifications</h4>
                <div class="list-group">
                    <% for (Notification notification : notifications) { %>
                        <div class="list-group-item">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1"><%= notification.getType() %></h5>
                                <small><%= notification.getDateCreation() %></small>
                            </div>
                            <p class="mb-1"><%= notification.getMessage() %></p>
                            <button class="btn btn-sm btn-outline-primary marquer-lu" 
                                    data-id="<%= notification.getId() %>">
                                Marquer comme lu
                            </button>
                        </div>
                    <% } %>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>
        
        <!-- Cartes de statistiques -->
        <%
            DashboardStats stats = (DashboardStats) request.getAttribute("stats");
            if (stats != null) {
        %>
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="card bg-primary text-white">
                        <div class="card-body">
                            <h5 class="card-title">Total Clients</h5>
                            <h2 class="card-text"><%= stats.getTotalClients() %></h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-success text-white">
                        <div class="card-body">
                            <h5 class="card-title">Total Prospects</h5>
                            <h2 class="card-text"><%= stats.getTotalProspects() %></h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-info text-white">
                        <div class="card-body">
                            <h5 class="card-title">Total Contrats</h5>
                            <h2 class="card-text"><%= stats.getTotalContrats() %></h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning text-white">
                        <div class="card-body">
                            <h5 class="card-title">Montant Total</h5>
                            <h2 class="card-text"><%= stats.getMontantTotalContrats() %>€</h2>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Graphiques -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Statut des Prospects</h5>
                            <canvas id="prospectsChart"></canvas>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Derniers Contrats</h5>
                            <canvas id="contratsChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Derniers éléments -->
            <div class="row">
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Derniers Clients</h5>
                        </div>
                        <div class="card-body">
                            <div class="list-group">
                                <% for (Client client : stats.getDerniersClients()) { %>
                                    <a href="${pageContext.request.contextPath}/client/view?id=<%= client.getId() %>" 
                                       class="list-group-item list-group-item-action">
                                        <%= client.getNom() %>
                                    </a>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Derniers Prospects</h5>
                        </div>
                        <div class="card-body">
                            <div class="list-group">
                                <% for (Prospect prospect : stats.getDerniersProspects()) { %>
                                    <a href="${pageContext.request.contextPath}/prospect/view?id=<%= prospect.getId() %>" 
                                       class="list-group-item list-group-item-action">
                                        <%= prospect.getNom() %>
                                    </a>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Derniers Contrats</h5>
                        </div>
                        <div class="card-body">
                            <div class="list-group">
                                <% for (Contrat contrat : stats.getDerniersContrats()) { %>
                                    <a href="${pageContext.request.contextPath}/contrat/view?id=<%= contrat.getId() %>" 
                                       class="list-group-item list-group-item-action">
                                        <%= contrat.getLibelle() %> - <%= contrat.getMontant() %>€
                                    </a>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Graphique des prospects
        const prospectsCtx = document.getElementById('prospectsChart').getContext('2d');
        new Chart(prospectsCtx, {
            type: 'pie',
            data: {
                labels: ['Nouveaux', 'En cours', 'Convertis'],
                datasets: [{
                    data: [
                        <%= stats.getProspectsNouveaux() %>,
                        <%= stats.getProspectsEnCours() %>,
                        <%= stats.getProspectsConvertis() %>
                    ],
                    backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
                }]
            }
        });

        // Graphique des contrats
        const contratsCtx = document.getElementById('contratsChart').getContext('2d');
        new Chart(contratsCtx, {
            type: 'bar',
            data: {
                labels: <%= stats.getDerniersContrats().stream()
                        .map(c -> c.getLibelle())
                        .collect(Collectors.toList()) %>,
                datasets: [{
                    label: 'Montant (€)',
                    data: <%= stats.getDerniersContrats().stream()
                            .mapToDouble(c -> c.getMontant())
                            .boxed()
                            .collect(Collectors.toList()) %>,
                    backgroundColor: '#4BC0C0'
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        // Gestion des notifications
        document.querySelectorAll('.marquer-lu').forEach(button => {
            button.addEventListener('click', function() {
                const id = this.dataset.id;
                fetch(`${pageContext.request.contextPath}/dashboard/notification/marquer-lu?id=${id}`, {
                    method: 'GET'
                }).then(response => {
                    if (response.ok) {
                        this.closest('.list-group-item').remove();
                    }
                });
            });
        });
    </script>
</body>
</html> 