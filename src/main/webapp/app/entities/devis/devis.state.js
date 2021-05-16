(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('devis', {
            parent: 'entity',
            url: '/devis',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.devis.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/devis/devis.html',
                    controller: 'DevisController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('devis');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('devis-detail', {
            parent: 'devis',
            url: '/devis/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.devis.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/devis/devis-detail.html',
                    controller: 'DevisDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('devis');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Devis', function($stateParams, Devis) {
                    return Devis.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'devis',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('devis-detail.edit', {
            parent: 'devis-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/devis/devis-dialog.html',
                    controller: 'DevisDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Devis', function(Devis) {
                            return Devis.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('devis.new', {
            parent: 'devis',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/devis/devis-dialog.html',
                    controller: 'DevisDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numero_Devis: null,
                                date_Devis: null,
                                montant: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('devis', null, { reload: 'devis' });
                }, function() {
                    $state.go('devis');
                });
            }]
        })
        .state('devis.edit', {
            parent: 'devis',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/devis/devis-dialog.html',
                    controller: 'DevisDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Devis', function(Devis) {
                            return Devis.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('devis', null, { reload: 'devis' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('devis.delete', {
            parent: 'devis',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/devis/devis-delete-dialog.html',
                    controller: 'DevisDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Devis', function(Devis) {
                            return Devis.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('devis', null, { reload: 'devis' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
