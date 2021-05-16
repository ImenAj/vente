(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commande-client', {
            parent: 'entity',
            url: '/commande-client',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.commandeClient.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commande-client/commande-clients.html',
                    controller: 'CommandeClientController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commandeClient');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('commande-client-detail', {
            parent: 'commande-client',
            url: '/commande-client/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.commandeClient.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commande-client/commande-client-detail.html',
                    controller: 'CommandeClientDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commandeClient');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CommandeClient', function($stateParams, CommandeClient) {
                    return CommandeClient.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'commande-client',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('commande-client-detail.edit', {
            parent: 'commande-client-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande-client/commande-client-dialog.html',
                    controller: 'CommandeClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommandeClient', function(CommandeClient) {
                            return CommandeClient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commande-client.new', {
            parent: 'commande-client',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande-client/commande-client-dialog.html',
                    controller: 'CommandeClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numeroCommande: null,
                                dateCommande: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('commande-client', null, { reload: 'commande-client' });
                }, function() {
                    $state.go('commande-client');
                });
            }]
        })
        .state('commande-client.edit', {
            parent: 'commande-client',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande-client/commande-client-dialog.html',
                    controller: 'CommandeClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommandeClient', function(CommandeClient) {
                            return CommandeClient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commande-client', null, { reload: 'commande-client' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commande-client.delete', {
            parent: 'commande-client',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande-client/commande-client-delete-dialog.html',
                    controller: 'CommandeClientDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CommandeClient', function(CommandeClient) {
                            return CommandeClient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commande-client', null, { reload: 'commande-client' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
