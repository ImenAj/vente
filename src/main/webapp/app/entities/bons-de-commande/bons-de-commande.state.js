(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bons-de-commande', {
            parent: 'entity',
            url: '/bons-de-commande',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.bons_de_commande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bons-de-commande/bons-de-commandes.html',
                    controller: 'Bons_de_commandeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bons_de_commande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bons-de-commande-detail', {
            parent: 'bons-de-commande',
            url: '/bons-de-commande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.bons_de_commande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bons-de-commande/bons-de-commande-detail.html',
                    controller: 'Bons_de_commandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bons_de_commande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bons_de_commande', function($stateParams, Bons_de_commande) {
                    return Bons_de_commande.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bons-de-commande',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bons-de-commande-detail.edit', {
            parent: 'bons-de-commande-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-commande/bons-de-commande-dialog.html',
                    controller: 'Bons_de_commandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bons_de_commande', function(Bons_de_commande) {
                            return Bons_de_commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bons-de-commande.new', {
            parent: 'bons-de-commande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-commande/bons-de-commande-dialog.html',
                    controller: 'Bons_de_commandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                referance: null,
                                etat: null,
                                site: null,
                                date_commandeE: null,
                                date_livraison_prevue: null,
                                livre: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bons-de-commande', null, { reload: 'bons-de-commande' });
                }, function() {
                    $state.go('bons-de-commande');
                });
            }]
        })
        .state('bons-de-commande.edit', {
            parent: 'bons-de-commande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-commande/bons-de-commande-dialog.html',
                    controller: 'Bons_de_commandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bons_de_commande', function(Bons_de_commande) {
                            return Bons_de_commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bons-de-commande', null, { reload: 'bons-de-commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bons-de-commande.delete', {
            parent: 'bons-de-commande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-commande/bons-de-commande-delete-dialog.html',
                    controller: 'Bons_de_commandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bons_de_commande', function(Bons_de_commande) {
                            return Bons_de_commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bons-de-commande', null, { reload: 'bons-de-commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
