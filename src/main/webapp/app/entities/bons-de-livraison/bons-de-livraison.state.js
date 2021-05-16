(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bons-de-livraison', {
            parent: 'entity',
            url: '/bons-de-livraison',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.bons_de_livraison.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bons-de-livraison/bons-de-livraisons.html',
                    controller: 'Bons_de_livraisonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bons_de_livraison');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bons-de-livraison-detail', {
            parent: 'bons-de-livraison',
            url: '/bons-de-livraison/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.bons_de_livraison.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bons-de-livraison/bons-de-livraison-detail.html',
                    controller: 'Bons_de_livraisonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bons_de_livraison');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bons_de_livraison', function($stateParams, Bons_de_livraison) {
                    return Bons_de_livraison.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bons-de-livraison',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bons-de-livraison-detail.edit', {
            parent: 'bons-de-livraison-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-livraison/bons-de-livraison-dialog.html',
                    controller: 'Bons_de_livraisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bons_de_livraison', function(Bons_de_livraison) {
                            return Bons_de_livraison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bons-de-livraison.new', {
            parent: 'bons-de-livraison',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-livraison/bons-de-livraison-dialog.html',
                    controller: 'Bons_de_livraisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                designation: null,
                                quantite_commandees: null,
                                date_livraison: null,
                                objet: null,
                                piece_jointe: null,
                                piece_jointeContentType: null,
                                reference: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bons-de-livraison', null, { reload: 'bons-de-livraison' });
                }, function() {
                    $state.go('bons-de-livraison');
                });
            }]
        })
        .state('bons-de-livraison.edit', {
            parent: 'bons-de-livraison',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-livraison/bons-de-livraison-dialog.html',
                    controller: 'Bons_de_livraisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bons_de_livraison', function(Bons_de_livraison) {
                            return Bons_de_livraison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bons-de-livraison', null, { reload: 'bons-de-livraison' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bons-de-livraison.delete', {
            parent: 'bons-de-livraison',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-livraison/bons-de-livraison-delete-dialog.html',
                    controller: 'Bons_de_livraisonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bons_de_livraison', function(Bons_de_livraison) {
                            return Bons_de_livraison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bons-de-livraison', null, { reload: 'bons-de-livraison' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
